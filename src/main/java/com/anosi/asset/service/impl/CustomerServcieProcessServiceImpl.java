package com.anosi.asset.service.impl;

import java.text.MessageFormat;
import java.util.Date;

import javax.transaction.Transactional;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.anosi.asset.component.SessionUtil;
import com.anosi.asset.dao.jpa.BaseJPADao;
import com.anosi.asset.dao.jpa.CustomerServiceProcessDao;
import com.anosi.asset.model.jpa.Account;
import com.anosi.asset.model.jpa.CustomerServiceProcess;
import com.anosi.asset.model.jpa.CustomerServiceProcess.EvaluatingDetail;
import com.anosi.asset.model.jpa.CustomerServiceProcess.RepairDetail;
import com.anosi.asset.model.jpa.CustomerServiceProcess.StartDetail;
import com.anosi.asset.model.jpa.MessageInfo;
import com.anosi.asset.model.jpa.ProcessRecord;
import com.anosi.asset.model.jpa.BaseProcess.FinishType;
import com.anosi.asset.model.jpa.ProcessRecord.HandleType;
import com.anosi.asset.service.CustomerServcieProcessService;
import com.anosi.asset.service.FileMetaDataService;
import com.google.common.collect.ImmutableMap;

@Service("customerServcieProcessService")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Transactional
public class CustomerServcieProcessServiceImpl extends BaseProcessServiceImpl<CustomerServiceProcess>
		implements CustomerServcieProcessService {

	private static final Logger logger = LoggerFactory.getLogger(CustomerServcieProcessServiceImpl.class);

	@Autowired
	private CustomerServiceProcessDao customerServiceProcessDao;
	@Autowired
	private FileMetaDataService fileMetaDataService;

	public CustomerServcieProcessServiceImpl() {
		super();
		// 在构造方法中对流程定义赋值
		definitionKey = "customerService";
	}

	@Override
	public BaseJPADao<CustomerServiceProcess> getRepository() {
		return customerServiceProcessDao;
	}

	@Override
	public CustomerServiceProcess findByProcessInstanceId(String processInstanceId) {
		logger.debug("findByProcessInstanceId:{}", processInstanceId);
		return customerServiceProcessDao.findByProcessInstanceId(processInstanceId);
	}

	@Override
	public void startProcess(Account engineeDep, StartDetail startDetail, MultipartFile[] multipartFiles)
			throws Exception {
		// 启动流程，因为下一步为完善清单，所以将发起人设置为下一步的办理人
		identityService.setAuthenticatedUserId(SessionUtil.getCurrentUser().getLoginId());
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(getDefinitionKey());
		CustomerServiceProcess customerServiceProcess = new CustomerServiceProcess();
		customerServiceProcess.setProcessInstanceId(processInstance.getId());
		customerServiceProcess.setFinishType(FinishType.REAMIN);
		customerServiceProcess.setApplicant(SessionUtil.getCurrentUser());
		customerServiceProcess.setFile(true);
		customerServiceProcessDao.save(customerServiceProcess);
		// 创建记录
		createNewProcessRecord(processInstance.getId());

		if (multipartFiles != null && multipartFiles.length != 0) {
			for (MultipartFile multipartFile : multipartFiles) {
				this.fileMetaDataService.saveFile("customerService_" + customerServiceProcess.getName(),
						multipartFile.getOriginalFilename(), multipartFile.getInputStream(), multipartFile.getSize());
			}
		}

		// 自动完成清单任务
		completeStartDetail(engineeDep,
				taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult().getId(),
				startDetail);
	}

	@Override
	public void completeStartDetail(Account engineeDep, String taskId, StartDetail startDetail) {
		findBytaskId(taskId).setStartDetail(startDetail);
		completeTask(taskId,
				() -> taskService.complete(taskId, ImmutableMap.of("engineeDep", engineeDep.getLoginId())));
	}

	@Override
	public void evaluating(Account servicer, String taskId, EvaluatingDetail evaluatingDetail) {
		findBytaskId(taskId).setEvaluatingDetail(evaluatingDetail);
		completeTask(taskId, () -> taskService.complete(taskId, ImmutableMap.of("servicer", servicer.getLoginId())));
	}

	@Override
	public void distribute(Account engineer, String taskId) {
		completeTask(taskId, () -> taskService.complete(taskId, ImmutableMap.of("engineer", engineer.getLoginId())));
	}

	@Override
	public void repair(String taskId, RepairDetail repairDetail) {
		CustomerServiceProcess customerServiceProcess = findBytaskId(taskId);
		customerServiceProcess.setRepairDetail(repairDetail);
		customerServiceProcess.setFinishType(FinishType.FINISHED);
		completeTask(taskId, () -> taskService.complete(taskId));
	}

	@Override
	public void entrust(String taskId, Account mandatary, String reason) {
		taskService.setAssignee(taskId, mandatary.getLoginId());// 任务委托
		// 完成相应的流程记录
		entrustProcessRecord(taskId, mandatary, reason);
		// 发送委托站内信
		entrustMessageInfo(taskId, mandatary, reason);
	}

	/***
	 * 委托以后发送站内信,发送给被委托的人
	 * 
	 * @param taskId
	 * @param mandatary
	 */
	private void entrustMessageInfo(String taskId, Account mandatary, String reason) {
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
				messageInfo.getFrom().getName(), customerServiceProcess.getName(), task.getName(), reason));

		messageInfos.add(messageInfo);
		saveMessageInfoAndSend();
	}

	/**
	 * 完成委托相应的流程记录
	 * 
	 * @param taskId
	 * @param mandatary
	 */
	private void entrustProcessRecord(String taskId, Account mandatary, String reason) {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		// 找出这个任务的记录,设置任务的完成时间和完成类型
		ProcessRecord unFinishRecord = processRecordService.findByTaskIdNotEnd(taskId);

		unFinishRecord.setType(HandleType.ENTRUST);
		unFinishRecord.setEndTime(new Date());
		unFinishRecord.setReason(reason);
		unFinishRecord.setAssignee(SessionUtil.getCurrentUser());

		// 生成新的流程记录
		ProcessRecord newRecord = new ProcessRecord();
		newRecord.setProcessInstanceId(task.getProcessInstanceId());
		newRecord.setTaskId(task.getId());
		newRecord.setTaskName(task.getName());
		newRecord.setStartTime(new Date());
		newRecord.setType(HandleType.REAMIN_TO_DO);
		newRecord.setRemain(mandatary.getLoginId());// 待办人
		processRecordService.save(newRecord);
	}

}
