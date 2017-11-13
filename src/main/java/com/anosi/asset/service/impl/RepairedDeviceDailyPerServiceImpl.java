package com.anosi.asset.service.impl;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.anosi.asset.dao.jpa.BaseJPADao;
import com.anosi.asset.dao.jpa.RepairedDeviceDailyPerDao;
import com.anosi.asset.model.jpa.CustomerServiceProcess;
import com.anosi.asset.model.jpa.DevCategory;
import com.anosi.asset.model.jpa.RepairedDeviceDailyPer;
import com.anosi.asset.service.CustomerServcieProcessService;
import com.anosi.asset.service.DevCategoryService;
import com.anosi.asset.service.RepairedDeviceDailyPerService;
import com.anosi.asset.util.DateFormatUtil;

@Service("repairedDeviceDailyPerService")
@Transactional
public class RepairedDeviceDailyPerServiceImpl extends BaseJPAServiceImpl<RepairedDeviceDailyPer>
		implements RepairedDeviceDailyPerService {

	@Autowired
	private RepairedDeviceDailyPerDao repairedDeviceDailyPerDao;
	@Autowired
	private DevCategoryService devCategoryService;
	@Autowired
	private CustomerServcieProcessService customerServcieProcessService;
	@Autowired
	private HistoryService historyService;

	@Override
	public BaseJPADao<RepairedDeviceDailyPer> getRepository() {
		return repairedDeviceDailyPerDao;
	}

	@Override
	public void saveRepairedPerDaily() {
		// 获取前一天的0点
		Calendar cal = Calendar.getInstance();// 得到一个Calendar的实例
	    cal.setTime(new Date()); // 设置时间为当前时间
	    cal.add(Calendar.DATE, -1); // 日期减1
	    cal.set(Calendar.HOUR_OF_DAY, 0);  
	    cal.set(Calendar.SECOND, 0);  
	    cal.set(Calendar.MINUTE, 0);  
	    cal.set(Calendar.MILLISECOND, 0);  
	    Date lastDate = cal.getTime();

		List<HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery()
				.processDefinitionKey("customerService").taskDefinitionKey("repair").finished()
				.taskCompletedBefore(new Date()).taskCompletedAfter(lastDate).list();

		List<String> processInstanceIds = tasks.stream().map(task -> task.getProcessInstanceId())
				.collect(Collectors.toList());

		devCategoryService.findAll().forEach(devCategory -> {
			RepairedDeviceDailyPer rp = new RepairedDeviceDailyPer();
			rp.setCountDate(lastDate);
			rp.setDevCategory(devCategory);
			rp.setRepaired(customerServcieProcessService.countByDevCategoryAndInstanceId(devCategory.getId(),
					processInstanceIds)); // 设置维修数量
			repairedDeviceDailyPerDao.save(rp);
			// 设置具体的维修设备
			List<CustomerServiceProcess> cps = customerServcieProcessService
					.findByDevCategoryAndInstanceId(devCategory.getId(), processInstanceIds);
			for (CustomerServiceProcess cp : cps) {
				rp.getDeviceList().add(cp.getDevice());
			}
		});
	}

	@Override
	public JSONObject findToReport(Pageable pageable) {
		JSONObject jsonObject = new JSONObject();
		JSONArray dateArray = new JSONArray();
		List<DevCategory> devCategoryList = devCategoryService.findAll();
		for (DevCategory devCategory : devCategoryList) {
			List<RepairedDeviceDailyPer> rps = repairedDeviceDailyPerDao.findByDevCategoryIdEquals(devCategory.getId(),
					pageable);
			// 倒序
			Collections.reverse(rps);
			JSONArray jsonArray = new JSONArray();
			for (RepairedDeviceDailyPer rp : rps) {
				jsonArray.add(rp.getRepaired());
				if (!jsonObject.containsKey("date")) {
					dateArray.add(DateFormatUtil.getFormateDate(rp.getCountDate(), "yyyy-MM-dd"));
				}
			}
			if (!jsonObject.containsKey("date")) {
				jsonObject.put("date", dateArray);
			}
			jsonObject.put(devCategory.getCategoryType().toString(), jsonArray);
		}
		return jsonObject;
	}

}
