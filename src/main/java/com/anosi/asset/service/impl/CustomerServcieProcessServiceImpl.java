package com.anosi.asset.service.impl;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.anosi.asset.component.I18nComponent;
import com.anosi.asset.dao.jpa.BaseJPADao;
import com.anosi.asset.dao.jpa.CustomerServiceProcessDao;
import com.anosi.asset.exception.CustomRunTimeException;
import com.anosi.asset.model.jpa.Account;
import com.anosi.asset.model.jpa.BaseProcess.FinishType;
import com.anosi.asset.model.jpa.CustomerServiceProcess;
import com.anosi.asset.model.jpa.CustomerServiceProcess.AgreementStatus.Agreement;
import com.anosi.asset.model.jpa.CustomerServiceProcess.RepairDetail;
import com.anosi.asset.model.jpa.MessageInfo;
import com.anosi.asset.model.jpa.ProcessRecord;
import com.anosi.asset.model.jpa.ProcessRecord.HandleType;
import com.anosi.asset.model.jpa.QCustomerServiceProcess;
import com.anosi.asset.service.CustomerServcieProcessService;
import com.anosi.asset.service.FileMetaDataService;
import com.anosi.asset.service.TechnologyDocumentService;
import com.google.common.collect.ImmutableMap;
import com.querydsl.core.types.Predicate;

@Service("customerServcieProcessService")
public class CustomerServcieProcessServiceImpl extends BaseProcessServiceImpl<CustomerServiceProcess>
		implements CustomerServcieProcessService {

	private static final Logger logger = LoggerFactory.getLogger(CustomerServcieProcessServiceImpl.class);

	@Autowired
	private CustomerServiceProcessDao customerServiceProcessDao;
	@Autowired
	private FileMetaDataService fileMetaDataService;
	@Autowired
	private EntityManager entityManager;
	@Autowired
	private AsyncDocument asyncDocument;

	public CustomerServcieProcessServiceImpl() {
		super();
		// 在构造方法中对流程定义赋值
		definitionKey = "customerService";
		detailUrl = "/customerServiceProcess/process/detail/view?id=";
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

	@Transactional
	@Override
	public void startProcess(CustomerServiceProcess process, MultipartFile[] multipartFiles) throws Exception {
		ProcessInstance processInstance;
		// 启动流程，因为下一步为完善清单，所以将发起人设置为下一步的办理人
		if (process.getId() == null) {
			identityService.setAuthenticatedUserId(sessionComponent.getCurrentUser().getLoginId());
			processInstance = runtimeService.startProcessInstanceByKey(getDefinitionKey());
			process.setProcessInstanceId(processInstance.getId());
			process.setFinishType(FinishType.REMAIN);
			process.setApplicant(sessionComponent.getCurrentUser());

			customerServiceProcessDao.save(process);
		} else {
			process.setFinishType(FinishType.REMAIN);
			process.getExamineDetail().setReject(false);
			processInstance = runtimeService.createProcessInstanceQuery()
					.processInstanceId(process.getProcessInstanceId()).singleResult();
		}

		// 创建记录
		createNewProcessRecord(processInstance.getId(), null);

		if (multipartFiles != null && multipartFiles.length != 0) {
			process.setFile(true);
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

	@Transactional
	@Override
	public void completeStartDetail(String taskId, CustomerServiceProcess process) throws Exception {

		// 判断是不是工程部的人
		String loginId = sessionComponent.getCurrentUser().getLoginId();
		Account currentAccount = accountService.findByLoginId(loginId);
		String code = currentAccount.getDepartment().getCode();
		// 如果是工程部
		if ("engineerDep".equals(code)) {
			process.setEngineeDep(accountService.findByLoginId(process.getStartDetail().getNextAssignee()));
			process.setFinishType(FinishType.REMAIN);
			completeTask(taskId, () -> taskService.complete(taskId,
					ImmutableMap.of("engineeDep", process.getStartDetail().getNextAssignee(), "isEnginee", true)),
					new ArrayList<>());
		} else {
			process.setNextAssignee(accountService.findByLoginId(process.getStartDetail().getNextAssignee()));
			completeTask(taskId, () -> taskService.complete(taskId,
					ImmutableMap.of("depManager", process.getStartDetail().getNextAssignee(), "isEnginee", false)),
					new ArrayList<>());
		}
	}

	@Transactional
	@Override
	public void examine(String taskId, CustomerServiceProcess process) throws Exception {
		if (process.getExamineDetail().getReject()) {
			HandleType type = HandleType.REFUSE;
			String reason = process.getExamineDetail().getSuggestion();
			process.setFinishType(FinishType.REFUSED);
			completeTask(taskId, () -> {
				// 被驳回，回退到完成工单节点
				try {
					this.turnTransition(taskId, "completeStartDetail", null);
				} catch (Exception e) {
					throw new CustomRunTimeException();
				}
			}, type, reason, new ArrayList<>());
		} else {
			process.setEngineeDep(accountService.findByLoginId(process.getExamineDetail().getEngineeDep()));
			completeTask(taskId,
					() -> taskService.complete(taskId,
							ImmutableMap.of("engineeDep", process.getExamineDetail().getEngineeDep())),
					new ArrayList<>());
		}
	}

	@Transactional
	@Override
	public void evaluating(String taskId, CustomerServiceProcess process) throws Exception {
		process.setServicer(accountService.findByLoginId(process.getEvaluatingDetail().getServicer()));
		completeTask(taskId, () -> taskService.complete(taskId,
				ImmutableMap.of("servicer", process.getEvaluatingDetail().getServicer())), new ArrayList<>());
	}

	@Transactional
	@Override
	public void distribute(String taskId, CustomerServiceProcess process) throws Exception {
		RepairDetail repairDetail = process.getRepairDetail();
		if (repairDetail == null) {
			repairDetail = new RepairDetail();
		}
		repairDetail.setRepairer(process.getDistributeDetail().getEngineer());
		process.setRepairDetail(repairDetail);
		process.setEngineer(accountService.findByLoginId(process.getDistributeDetail().getEngineer()));
		process.setRepairer(process.getEngineer());
		completeTask(taskId, () -> taskService.complete(taskId,
				ImmutableMap.of("engineer", process.getDistributeDetail().getEngineer())), new ArrayList<>());
	}

	@Override
	public void repair(String taskId, CustomerServiceProcess process) throws Exception {
		repairActual(taskId, process);
		asyncDocument.insertIntoDocument(process);
	}

	@Transactional
	public void repairActual(String taskId, CustomerServiceProcess process) throws Exception {
		process.setFinishType(FinishType.FINISHED);
		process.setFinishDate(new Date());
		customerServiceProcessDao.save(process);
		process.getRepairDetail().setRepairTime(new Date());
		completeTask(taskId, () -> taskService.complete(taskId), new ArrayList<>());
	}

	@Transactional
	@Override
	public void entrust(String taskId, Account mandatary, String reason, CustomerServiceProcess process) {
		process.getRepairDetail().setRepairer(mandatary.getName());
		process.setRepairer(mandatary);
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

		List<MessageInfo> messageInfos = new ArrayList<>();
		messageInfos.add(messageInfo);
		saveMessageInfoAndSend(messageInfos);
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
		newRecord.setType(HandleType.REMAIN_TO_DO);
		newRecord.setRemain(mandatary.getLoginId());// 待办人
		processRecordService.save(newRecord);
	}

	@Override
	public List<String> getProcessInstanceIdsBySearchContent(String searchContent) {
		Page<CustomerServiceProcess> contents = customerServiceProcessDao.findBySearchContent(entityManager,
				searchContent, null);
		// 所有CustomerServiceProcess的id
		List<Long> ids = contents.getContent().parallelStream().map(c -> c.getId()).collect(Collectors.toList());
		List<CustomerServiceProcess> allCustomerServiceProcesses = customerServiceProcessDao.findAll(ids);
		// 所有processInstanceIds
		List<String> processInstanceIds = allCustomerServiceProcesses.parallelStream()
				.map(c -> c.getProcessInstanceId()).collect(Collectors.toList());
		return processInstanceIds;
	}

	@Override
	public JSONArray getTaskDistribute() {
		List<Object[]> countTaskDistribute = customerServiceProcessDao.countTaskDistribute(definitionKey);

		JSONArray jsonArray = new JSONArray();
		for (Object[] counts : countTaskDistribute) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("name", counts[0]);
			jsonObject.put("count", counts[1]);
			jsonArray.add(jsonObject);
		}

		return jsonArray;
	}

	@Override
	public Long countByDevCategoryAndInstanceId(Long id, List<String> processInstanceIds) {
		return customerServiceProcessDao.countByDevice_devCategory_idEqualsAndProcessInstanceIdIn(id,
				processInstanceIds);
	}

	@Override
	public List<CustomerServiceProcess> findByDevCategoryAndInstanceId(Long id, List<String> processInstanceIds) {
		return customerServiceProcessDao.findByDevice_devCategory_idEqualsAndProcessInstanceIdIn(id,
				processInstanceIds);
	}

	@Override
	public Predicate convertPredicate(Predicate predicate, String timeType, Date beginTime, Date endTime,
			Agreement agreement) {
		QCustomerServiceProcess qc = QCustomerServiceProcess.customerServiceProcess;
		if (beginTime != null) {
			if ("start".equals(timeType)) {
				predicate = qc.createDate.after(beginTime).and(predicate);
			} else if ("end".equals(timeType)) {
				predicate = qc.finishDate.after(beginTime).and(predicate);
			}
		}
		if (endTime != null) {
			if ("start".equals(timeType)) {
				predicate = qc.createDate.before(endTime).and(predicate);
			} else if ("end".equals(timeType)) {
				predicate = qc.finishDate.before(endTime).and(predicate);
			}
		}

		if (agreement != null) {
			switch (agreement) {
			case UNCOMFIRMED:
				predicate = qc.agreementStatus.endTime.isNull().and(predicate);
				break;
			case COMFIRMED:
				predicate = qc.agreementStatus.endTime.isNotNull().and(predicate);
				break;
			default:
				break;
			}
		}
		return predicate;
	}

	@Override
	public Page<CustomerServiceProcess> findbyContent(String searchContent, Pageable pageable) {
		return customerServiceProcessDao.findBySearchContent(entityManager, searchContent, pageable);
	}

	@Component
	public static class AsyncDocument {
		
		@Autowired
		private TechnologyDocumentService technologyDocumentService;
		@Autowired
		private I18nComponent i18nComponent;
		
		/***
		 * 将维修方案写入文档管理库
		 * 
		 * @param process
		 * @throws Exception
		 */
		@Async
		public void insertIntoDocument(CustomerServiceProcess process) throws Exception {
			String problemDescription = process.getRepairDetail().getProblemDescription();
			String failureCause = process.getRepairDetail().getFailureCause();
			String processMode = process.getRepairDetail().getProcessMode();

			StringBuilder sb = new StringBuilder();
			sb.append(i18nComponent.getMessage("customerService.problemDescription")).append(":")
					.append(problemDescription).append("\n");
			sb.append(i18nComponent.getMessage("customerService.failureCause")).append(":").append(failureCause)
					.append("\n");
			sb.append(i18nComponent.getMessage("customerService.processMode")).append(":").append(processMode)
					.append("\n");

			InputStream is = IOUtils.toInputStream(sb.toString(), Charset.forName("UTF-8"));

			technologyDocumentService.createTechnologyDocument(process.getName() + ".txt", is,
					((Number) sb.length()).longValue(), "故障规则", "故障规则");
		}
	}

}
