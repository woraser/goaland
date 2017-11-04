package com.anosi.asset.service.impl;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.anosi.asset.component.MapComponent;
import com.anosi.asset.dao.jpa.BaseJPADao;
import com.anosi.asset.dao.jpa.DeviceDao;
import com.anosi.asset.exception.CustomRunTimeException;
import com.anosi.asset.model.elasticsearch.DeviceContent;
import com.anosi.asset.model.jpa.Device;
import com.anosi.asset.model.jpa.District;
import com.anosi.asset.model.jpa.QDevice;
import com.anosi.asset.service.DeviceContentService;
import com.anosi.asset.service.DeviceService;
import com.anosi.asset.util.MapUtil;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathInits;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Service("deviceService")
@Transactional
public class DeviceServiceImpl extends BaseJPAServiceImpl<Device> implements DeviceService {

	private static final Logger logger = LoggerFactory.getLogger(DeviceServiceImpl.class);

	@Autowired
	private DeviceDao deviceDao;
	@Autowired
	private MapComponent mapComponent;
	@Autowired
	private EntityManager entityManager;
	@Autowired
	private DeviceContentService deviceContentService;

	@Override
	public BaseJPADao<Device> getRepository() {
		return deviceDao;
	}

	@Override
	public Device findBySerialNo(String serialNo) {
		return deviceDao.findBySerialNo(serialNo);
	}

	/***
	 * 重写save,保存device的同时，将@Content标记的字段内容提取，存储到deviceContent中
	 * 
	 */
	@Override
	public <S extends Device> S save(S device) {
		device = deviceDao.save(device);

		try {
			deviceContentService.saveContent(device);
		} catch (Exception e) {
			throw new CustomRunTimeException(e.getMessage());
		}
		return device;
	}

	/***
	 * 重写批量添加
	 */
	@Override
	public <S extends Device> Iterable<S> save(Iterable<S> devices) {
		devices = deviceDao.save(devices);
		try {
			deviceContentService.saveContent(devices);
		} catch (Exception e) {
			throw new CustomRunTimeException(e.getMessage());
		}

		return devices;
	}

	/***
	 * 重写删除
	 */
	@Override
	public void delete(Long id) {
		super.delete(id);
		deviceContentService.delete(id.toString());// 删除模糊搜索的索引
	};

	/***
	 * 重写删除
	 */
	@Override
	public void delete(Device entity) {
		super.delete(entity);
		deviceContentService.delete(entity.getId().toString());// 删除模糊搜索的索引
	}

	@Override
	public Device setDeviceDistrict(Device device) {
		Double longitude = device.getLongitude();
		Double latitude = device.getLatitude();
		if (longitude == null) {
			throw new RuntimeException(i18nComponent.getMessage("device.longitude.cannot.null"));
		}
		if (latitude == null) {
			throw new RuntimeException(i18nComponent.getMessage("device.latitude.cannot.null"));
		}
		JSONObject addressComponent = MapUtil.getAddressComponent(String.valueOf(longitude), String.valueOf(latitude));
		District district = mapComponent.createMap(addressComponent);
		device.setDistrict(district);
		// 获取转换的百度坐标
		JSONObject convertLocation = MapUtil.convertLocation(String.valueOf(longitude), String.valueOf(latitude));
		device.setBaiduLongitude(
				Double.parseDouble(new String(Base64.getDecoder().decode(convertLocation.getString("x")))));
		device.setBaiduLatitude(
				Double.parseDouble(new String(Base64.getDecoder().decode(convertLocation.getString("y")))));
		return device;
	}

	/***
	 * 基本思路是count groupBy province,city 查看结果是否为1,为1表示都是同一个行政区划的,那么找下一级
	 * 
	 * @param predicate
	 */
	@Override
	public JSONArray ascertainArea(Predicate predicate) {
		QDevice qDevice = QDevice.device;
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
		List<Tuple> iotxTuples = null;

		QDevice qDeviceCustom = createQDeviceCustom();
		long countCountry = queryFactory.from(qDeviceCustom).where(predicate)
				.groupBy(qDeviceCustom.district.city.province.country).fetchCount();
		if (countCountry == 1) {
			// 如果都是一个国家,那么查看是否都是一个省
			long countProvince = queryFactory.from(qDeviceCustom).where(predicate)
					.groupBy(qDeviceCustom.district.city.province).fetchCount();
			if (countProvince == 1) {
				// 如果都是一个省，那么查看是否都是一个市
				long countCity = queryFactory.from(qDevice).where(predicate).groupBy(qDevice.district.city)
						.fetchCount();
				if (countCity == 1) {
					// 如果都是一个市，那么就按照区来统计数据
					iotxTuples = queryFactory.select(qDevice.district.name, qDevice.count()).from(qDevice)
							.where(predicate).groupBy(qDevice.district).fetch();
				} else {
					// 如果是多个市，就按照市统计
					iotxTuples = queryFactory.select(qDevice.district.city.name, qDevice.count()).from(qDevice)
							.where(predicate).groupBy(qDevice.district.city).fetch();
				}
			} else {
				// 如果是多个省，就按照省统计
				iotxTuples = queryFactory.select(qDeviceCustom.district.city.province.name, qDevice.count())
						.from(qDevice).where(predicate).groupBy(qDeviceCustom.district.city.province).fetch();
			}
		} else {
			iotxTuples = queryFactory.select(qDeviceCustom.district.city.province.country.name, qDevice.count())
					.from(qDevice).where(predicate).groupBy(qDeviceCustom.district.city.province.country).fetch();
		}

		JSONArray jsonArray = new JSONArray();
		for (Tuple tuple : iotxTuples) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("name", tuple.get(0, String.class));
			jsonObject.put("amount", tuple.get(1, Long.class));
			jsonArray.add(jsonObject);
		}
		return jsonArray;
	}

	/***
	 * 主要在于构造PathInits 需要有如下结构: map:{country:{district:{city:{province:.....}}}}
	 */
	private QDevice createQDeviceCustom() {
		PathInits inits = new PathInits("district.city.province.country");
		QDevice qDevice = new QDevice(Device.class, forVariable("device"), inits);
		return qDevice;
	}

	@Override
	public Page<Device> findByContentSearch(String searchContent, Pageable pageable) {
		// 防止sort报错，只获取pageable的页数和size
		logger.debug("page:{},size:{}", pageable.getPageNumber(), pageable.getPageSize());
		Pageable contentPage = new PageRequest(pageable.getPageNumber(), pageable.getPageSize());
		Page<DeviceContent> deviceContents = deviceContentService.findByContent(searchContent, contentPage);
		List<Long> ids = deviceContents.getContent().stream().map(c -> Long.parseLong(c.getId()))
				.collect(Collectors.toList());
		return findAll(QDevice.device.id.in(ids), contentPage);
	}

}
