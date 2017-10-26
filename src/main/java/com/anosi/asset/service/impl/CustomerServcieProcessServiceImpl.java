package com.anosi.asset.service.impl;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.anosi.asset.dao.jpa.BaseJPADao;
import com.anosi.asset.dao.jpa.CustomerServiceProcessDao;
import com.anosi.asset.model.elasticsearch.CustomerServiceProcessContent;
import com.anosi.asset.model.jpa.Account;
import com.anosi.asset.model.jpa.BaseProcess.FinishType;
import com.anosi.asset.model.jpa.CustomerServiceProcess;
import com.anosi.asset.model.jpa.MessageInfo;
import com.anosi.asset.model.jpa.ProcessRecord;
import com.anosi.asset.model.jpa.ProcessRecord.HandleType;
import com.anosi.asset.service.CustomerServcieProcessService;
import com.anosi.asset.service.CustomerServiceProcessContentService;
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
	@Autowired
	private CustomerServiceProcessContentService customerServiceProcessContentService;

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
	public void startProcess(CustomerServiceProcess process, MultipartFile[] multipartFiles) throws Exception {
		// 启动流程，因为下一步为完善清单，所以将发起人设置为下一步的办理人
		identityService.setAuthenticatedUserId(sessionComponent.getCurrentUser().getLoginId());
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(getDefinitionKey());
		process.setProcessInstanceId(processInstance.getId());
		process.setFinishType(FinishType.REAMIN);
		process.setApplicant(sessionComponent.getCurrentUser());
		process.setFile(true);
		
		customerServiceProcessDao.save(process);
		customerServiceProcessContentService.saveContent(process);

		// 创建记录
		createNewProcessRecord(processInstance.getId());

		if (multipartFiles != null && multipartFiles.length != 0) {
			for (MultipartFile multipartFile : multipartFiles) {
				this.fileMetaDataService.saveFile("customerService_" + process.getName(),
						multipartFile.getOriginalFilename(), multipartFile.getInputStream(), multipartFile.getSize());
			}
		}

		// 自动完成清单任务
		completeStartDetail(
				taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult().getId(),
				process);
	}

	@Override
	public void completeStartDetail(String taskId, CustomerServiceProcess process) {

		// 判断是不是工程部的人
		String loginId = sessionComponent.getCurrentUser().getLoginId();
		Account currentAccount = accountService.findByLoginId(loginId);
		String code = currentAccount.getDepartment().getCode();
		// 如果是工程部
		if ("engineerDep".equals(code)) {
			completeTask(taskId, () -> taskService.complete(taskId,
					ImmutableMap.of("engineeDep", process.getStartDetail().getNextAssignee(), "isEnginee", true)));
		} else {
			completeTask(taskId, () -> taskService.complete(taskId,
					ImmutableMap.of("depManager", process.getStartDetail().getNextAssignee(), "isEnginee", false)));
		}
	}

	@Override
	public void examine(String taskId, CustomerServiceProcess process) throws Exception {
		if (process.getExamineDetail().getReject()) {
			// 被驳回，回退到完成工单节点
			this.turnTransition(taskId, "completeStartDetail", null);
		} else {
			completeTask(taskId, () -> taskService.complete(taskId,
					ImmutableMap.of("engineeDep", process.getExamineDetail().getEngineeDep())));
		}
	}

	@Override
	public void evaluating(String taskId, CustomerServiceProcess process) {
		completeTask(taskId, () -> taskService.complete(taskId,
				ImmutableMap.of("servicer", process.getEvaluatingDetail().getServicer())));
	}

	@Override
	public void distribute(String taskId, CustomerServiceProcess process) {
		completeTask(taskId, () -> taskService.complete(taskId,
				ImmutableMap.of("engineer", process.getDistributeDetail().getEngineer())));
	}

	@Override
	public void repair(String taskId, CustomerServiceProcess process) {
		process.setFinishType(FinishType.FINISHED);
		completeTask(taskId, () -> taskService.complete(taskId));
	}

	@Override
	public void entrust(String taskId, Account mandatary, String reason, CustomerServiceProcess process) {
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
		messageInfo.setFrom(sessionComponent.getCurrentUser());
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
		unFinishRecord.setAssignee(sessionComponent.getCurrentUser());

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

	@Override
	public List<String> getProcessInstanceIdsBySearchContent(String searchContent) {
		List<CustomerServiceProcessContent> contents = customerServiceProcessContentService
				.findByContent(searchContent);
		List<String> ids = contents.parallelStream()
				.map(c -> customerServiceProcessDao.getOne(Long.parseLong(c.getId())).getProcessInstanceId())
				.collect(Collectors.toList());
		return ids;
	}

}
