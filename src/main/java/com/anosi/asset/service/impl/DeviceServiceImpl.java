package com.anosi.asset.service.impl;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

import java.util.Base64;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.TermQuery;
import org.hibernate.search.query.dsl.MustJunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.anosi.asset.component.MapComponent;
import com.anosi.asset.component.SessionComponent;
import com.anosi.asset.dao.hibernateSearch.SupplyQuery;
import com.anosi.asset.dao.jpa.BaseJPADao;
import com.anosi.asset.dao.jpa.DeviceDao;
import com.anosi.asset.model.jpa.Device;
import com.anosi.asset.model.jpa.District;
import com.anosi.asset.model.jpa.QDevice;
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

	@Override
	public BaseJPADao<Device> getRepository() {
		return deviceDao;
	}

	@Override
	public Device findBySerialNo(String serialNo) {
		return deviceDao.findBySerialNo(serialNo);
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
		logger.debug("page:{},size:{}", pageable.getPageNumber(), pageable.getPageSize());
		if (SessionComponent.isClient()) {
			SupplyQuery supplyQuery = (queryBuilder) -> {
				MustJunction mustJunction = queryBuilder.bool()
						.must(queryBuilder.keyword()
								.onFields("project.name",
										"project.number", "project.location", "productName", "productNo", "productSpecifications", "serialNo",
										"rfid")
								.matching(searchContent).createQuery())
						.must(new TermQuery(new Term("ownerList.loginId", sessionComponent.getCurrentUser().getLoginId())));
				return mustJunction.createQuery();
			};
			return deviceDao.findBySearchContent(entityManager, searchContent, pageable, Device.class, supplyQuery, "");
		} else {
			return deviceDao.findBySearchContent(entityManager, searchContent, pageable);
		}
	}
	
	@Override
	public Device findByRfid(String rfid){
		return deviceDao.findByRfid(rfid);
	}

	@Override
	public List<Device> findIdAndSN() {
		return deviceDao.findIdAndSN();
	}

}
