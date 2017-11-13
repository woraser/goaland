package com.anosi.asset.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.activiti.engine.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anosi.asset.dao.jpa.BaseJPADao;
import com.anosi.asset.dao.jpa.CustomerServiceProcessDailyPerDao;
import com.anosi.asset.model.jpa.CustomerServiceProcess;
import com.anosi.asset.model.jpa.CustomerServiceProcessDailyPer;
import com.anosi.asset.service.CustomerServcieProcessService;
import com.anosi.asset.service.CustomerServiceProcessDailyPerService;

@Service("customerServiceProcessDailyPerService")
@Transactional
public class CustomerServiceProcessDailyPerServiceImpl extends BaseJPAServiceImpl<CustomerServiceProcessDailyPer>
		implements CustomerServiceProcessDailyPerService {

	@Autowired
	private CustomerServiceProcessDailyPerDao customerServiceProcessDailyPerDao;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private CustomerServcieProcessService customerServcieProcessService;

	@Override
	public BaseJPADao<CustomerServiceProcessDailyPer> getRepository() {
		return customerServiceProcessDailyPerDao;
	}

	@Override
	public void saveProcessPerDaily() {
		long unCompleted = historyService.createHistoricProcessInstanceQuery().processDefinitionKey("customerService")
				.unfinished().count();
		long completed = historyService.createHistoricProcessInstanceQuery().processDefinitionKey("customerService")
				.finished().count();

		List<CustomerServiceProcess> unCompletedProcessList = historyService.createHistoricProcessInstanceQuery()
				.processDefinitionKey("customerService").unfinished().list().stream()
				.map(hps -> customerServcieProcessService.findByProcessInstanceId(hps.getId()))
				.collect(Collectors.toList());

		List<CustomerServiceProcess> completedProcessList = historyService.createHistoricProcessInstanceQuery()
				.processDefinitionKey("customerService").finished().list().stream()
				.map(hps -> customerServcieProcessService.findByProcessInstanceId(hps.getId()))
				.collect(Collectors.toList());

		// 获取前一天的0点
		Calendar cal = Calendar.getInstance();// 得到一个Calendar的实例
	    cal.setTime(new Date()); // 设置时间为当前时间
	    cal.add(Calendar.DATE, -1); // 日期减1
	    cal.set(Calendar.HOUR_OF_DAY, 0);  
	    cal.set(Calendar.SECOND, 0);  
	    cal.set(Calendar.MINUTE, 0);  
	    cal.set(Calendar.MILLISECOND, 0);  
	    Date lastDate = cal.getTime();

		CustomerServiceProcessDailyPer cs = new CustomerServiceProcessDailyPer();
		cs.setCompleted(completed);
		cs.setUnCompleted(unCompleted);
		cs.setCountDate(lastDate);
		customerServiceProcessDailyPerDao.save(cs);
		
		cs.setCompletedProcessList(completedProcessList);
		cs.setUnCompletedProcessList(unCompletedProcessList);
	}

}
