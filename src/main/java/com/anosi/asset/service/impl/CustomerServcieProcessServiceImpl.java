package com.anosi.asset.service.impl;

import java.text.MessageFormat;
import java.util.Date;

import javax.transaction.Transactional;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anosi.asset.component.SessionUtil;
import com.anosi.asset.dao.jpa.CustomerServiceProcessDao;
import com.anosi.asset.model.jpa.Account;
import com.anosi.asset.model.jpa.CustomerServiceProcess;
import com.anosi.asset.model.jpa.CustomerServiceProcess.EvaluatingDetail;
import com.anosi.asset.model.jpa.CustomerServiceProcess.RepairDetail;
import com.anosi.asset.model.jpa.CustomerServiceProcess.StartDetail;
import com.anosi.asset.model.jpa.MessageInfo;
import com.anosi.asset.service.CustomerServcieProcessService;
import com.google.common.collect.ImmutableMap;

@Service("customerServcieProcessService")
@Transactional
public class CustomerServcieProcessServiceImpl extends BaseProcessServiceImpl<CustomerServiceProcess>
		implements CustomerServcieProcessService {

	private static final Logger logger = LoggerFactory.getLogger(CustomerServcieProcessServiceImpl.class);

	@Autowired
	private CustomerServiceProcessDao customerServiceProcessDao;

	public CustomerServcieProcessServiceImpl() {
		super();
		// 在构造方法中对流程定义赋值
		definitionKey = "customerService";
	}

	@Override
	public CustomerServiceProcess findByProcessInstanceId(String processInstanceId) {
		logger.debug("findByProcessInstanceId:{}", processInstanceId);
		return customerServiceProcessDao.findByProcessInstanceId(processInstanceId);
	}

	@Override
	public void startProcess() {
		// 启动流程，因为下一步为完善清单，所以将发起人设置为下一步的办理人
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(getDefinitionKey(),
				ImmutableMap.of("assignee", SessionUtil.getCurrentUser().getLoginId()));
		CustomerServiceProcess customerServiceProcess = new CustomerServiceProcess();
		customerServiceProcess.setProcessInstanceId(processInstance.getId());
		customerServiceProcessDao.save(customerServiceProcess);
	}

	@Override
	public void completeStartDetail(Account engineeDep, String taskId, StartDetail startDetail) {
		CustomerServiceProcess customerServiceProcess = findBytaskId(taskId);
		customerServiceProcess.setStartDetail(startDetail);
		completeTask(taskId, customerServiceProcess, customerServiceProcess.getApplicant(), engineeDep,
				() -> taskService.complete(taskId, ImmutableMap.of("engineeDep", engineeDep.getLoginId())));
	}

	@Override
	public void evaluating(Account servicer, String taskId, EvaluatingDetail evaluatingDetail) {
		CustomerServiceProcess customerServiceProcess = findBytaskId(taskId);
		customerServiceProcess.setEvaluatingDetail(evaluatingDetail);
		completeTask(taskId, customerServiceProcess, customerServiceProcess.getApplicant(), servicer,
				() -> taskService.complete(taskId, ImmutableMap.of("servicer", servicer.getLoginId())));
	}

	@Override
	public void distribute(Account engineer, String taskId) {
		CustomerServiceProcess customerServiceProcess = findBytaskId(taskId);
		completeTask(taskId, customerServiceProcess, customerServiceProcess.getApplicant(), engineer,
				() -> taskService.complete(taskId, ImmutableMap.of("engineer", engineer.getLoginId())));
	}

	@Override
	public void repair(String taskId, RepairDetail repairDetail) {
		CustomerServiceProcess customerServiceProcess = findBytaskId(taskId);
		customerServiceProcess.setRepairDetail(repairDetail);
		completeTask(taskId, customerServiceProcess, customerServiceProcess.getApplicant(),
				() -> taskService.complete(taskId));
	}

	@Override
	public void entrust(String taskId, Account mandatary) {
		taskService.setAssignee(taskId, mandatary.getLoginId());// 任务委托
		entrustMessageInfo(taskId, mandatary);
	}

	/***
	 * 委托以后发送站内信,发送给被委托的人
	 * 
	 * @param taskId
	 * @param mandatary
	 */
	private void entrustMessageInfo(String taskId, Account mandatary) {
		CustomerServiceProcess customerServiceProcess = findBytaskId(taskId);
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		// 发送委托的站内信
		MessageInfo messageInfo = new MessageInfo();
		messageInfo.setFrom(SessionUtil.getCurrentUser());
		messageInfo.setTo(mandatary);
		messageInfo.setSendTime(new Date());
		// 从i18n中读取信息
		messageInfo.setTitle(i18nComponent.getMessage("message.titile.entrust"));
		messageInfo.setContent(MessageFormat.format(i18nComponent.getMessage("message.content.entrust"),
				messageInfo.getFrom().getName(), customerServiceProcess.getName(), task.getName()));

		messageInfos.add(messageInfo);
		saveMessageInfoAndSend();
	}

}
