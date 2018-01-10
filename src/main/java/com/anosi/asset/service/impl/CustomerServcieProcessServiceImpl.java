package com.anosi.asset.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.anosi.asset.dao.jpa.BaseJPADao;
import com.anosi.asset.dao.jpa.CustomerServiceProcessDao;
import com.anosi.asset.exception.CustomRunTimeException;
import com.anosi.asset.i18n.I18nComponent;
import com.anosi.asset.model.jpa.*;
import com.anosi.asset.model.jpa.AgreementStatus.Agreement;
import com.anosi.asset.model.jpa.BaseProcess.FinishType;
import com.anosi.asset.model.jpa.DocumentType.TypeValue;
import com.anosi.asset.model.jpa.ProcessRecord.HandleType;
import com.anosi.asset.service.*;
import com.google.common.collect.ImmutableMap;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
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

import javax.persistence.EntityManager;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

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
	@Autowired
	private StartDetailService startDetailService;
	@Autowired
	private ExamineDetailService examineDetailService;
	@Autowired
	private EvaluatingDetailService evaluatingDetailService;
	@Autowired
	private DistributeDetailService distributeDetailService;
	@Autowired
	private RepairDetailService repairDetailService;
	@Autowired
	private EntrustDetailService entrustDetailService;
	@Autowired
	private AccountService accountService;

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

			examineDetailService.save(process.getExamineDetail());
			startDetailService.save(process.getStartDetail());
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
		process.setFinishType(FinishType.REMAIN);
		// 如果是工程部
		if ("engineerDep".equals(code)) {
			completeTask(taskId,
					() -> taskService.complete(taskId, ImmutableMap.of("engineeDep",
							process.getStartDetail().getNextAssignee().getLoginId(), "isEnginee", true)),
					new ArrayList<>());
		} else {
			completeTask(taskId,
					() -> taskService.complete(taskId, ImmutableMap.of("depManager",
							process.getStartDetail().getNextAssignee().getLoginId(), "isEnginee", false)),
					new ArrayList<>());
		}
	}

	@Transactional
	@Override
	public void examine(String taskId, CustomerServiceProcess process) throws Exception {
		examineDetailService.save(process.getExamineDetail());
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
			completeTask(taskId,
					() -> taskService.complete(taskId,
							ImmutableMap.of("engineeDep", process.getExamineDetail().getEngineeDep().getLoginId())),
					new ArrayList<>());
		}
	}

	@Transactional
	@Override
	public void evaluating(String taskId, CustomerServiceProcess process) throws Exception {
		evaluatingDetailService.save(process.getEvaluatingDetail());
		completeTask(taskId,
				() -> taskService.complete(taskId,
						ImmutableMap.of("servicer", process.getEvaluatingDetail().getServicer().getLoginId())),
				new ArrayList<>());
	}

	@Transactional
	@Override
	public void distribute(String taskId, CustomerServiceProcess process) throws Exception {
		distributeDetailService.save(process.getDistributeDetail());
		completeTask(taskId,
				() -> taskService.complete(taskId,
						ImmutableMap.of("engineer", process.getDistributeDetail().getEngineer().getLoginId())),
				new ArrayList<>());
	}

	@Override
	@Transactional
	public void repair(String taskId, CustomerServiceProcess process, MultipartFile[] multipartFiles, Long[] devices,
			Long[] fellows, Long[] faultCategorys) throws Exception {
		// 上传文件
		if (multipartFiles != null && multipartFiles.length != 0) {
			for (MultipartFile multipartFile : multipartFiles) {
				this.fileMetaDataService.saveFile("customerService_repair_" + process.getName(),
						multipartFile.getOriginalFilename(), multipartFile.getInputStream(), multipartFile.getSize());
			}
		}

		// 设置关联设备
		for (Long deviceId : devices) {
			Device device = new Device();
			device.setId(deviceId);
			process.getRepairDetail().getDeviceList().add(device);
		}
		// 同行工程师
		if (fellows != null && fellows.length != 0) {
			for (Long fellowId : fellows) {
				Account account = new Account();
				account.setId(fellowId);
				process.getRepairDetail().getFellowList().add(account);
			}
		}
		// 故障分类
		for (Long faultCategoryId : faultCategorys) {
			FaultCategory faultCategory = new FaultCategory();
			faultCategory.setId(faultCategoryId);
			process.getRepairDetail().getFaultCategoryList().add(faultCategory);
		}

		repairDetailService.save(process.getRepairDetail());
		boolean entrust = process.getRepairDetail().getEntrust();
		Map<String, Object> map = new HashMap<>();
		map.put("entrust", entrust);
		if (entrust) {
			// 转派
			map.put("entruster", accountService.getOne(process.getRepairDetail().getEntruster().getId()).getLoginId());

			RepairDetail repairDetail = process.getRepairDetail();
			// 给entrustDetail赋初始值
			EntrustDetail entrustDetail = new EntrustDetail();
			entrustDetail.setProblemDescription(repairDetail.getProblemDescription());
			entrustDetail.setFailureCause(repairDetail.getFailureCause());
			entrustDetail.setProcessMode(repairDetail.getProcessMode());
			repairDetail.getDeviceList().forEach(device -> entrustDetail.getDeviceList().add(device));
			repairDetail.getFaultCategoryList()
					.forEach(faultCategory -> entrustDetail.getFaultCategoryList().add(faultCategory));

			entrustDetailService.save(entrustDetail);
			process.setEntrustDetail(entrustDetail);
		} else {
			process.setFinishType(FinishType.FINISHED);
			process.setFinishDate(new Date());
			// 把解决方案写入到方案库
			sessionComponent.getCurrentUser();// 利用shiro缓存机制，防止异步任务获取session出错
			asyncDocument.insertIntoDocument(process.getRepairDetail(), process);
		}
		customerServiceProcessDao.save(process);
		completeTask(taskId, () -> taskService.complete(taskId, map), new ArrayList<>());
	}

	@Override
	@Transactional
	public void entrust(String taskId, CustomerServiceProcess process, MultipartFile[] multipartFiles, Long[] devices,
			Long[] fellows, Long[] faultCategorys) throws Exception {
		if (multipartFiles != null && multipartFiles.length != 0) {
			for (MultipartFile multipartFile : multipartFiles) {
				this.fileMetaDataService.saveFile("customerService_entrust_" + process.getName(),
						multipartFile.getOriginalFilename(), multipartFile.getInputStream(), multipartFile.getSize());
			}
		}

		// 先清空关联设备
		process.getEntrustDetail().getDeviceList().clear();
		// 设置关联设备
		for (Long deviceId : devices) {
			Device device = new Device();
			device.setId(deviceId);
			process.getEntrustDetail().getDeviceList().add(device);
		}
		// 同行工程师
		if (fellows != null && fellows.length != 0) {
			for (Long fellowId : fellows) {
				Account account = new Account();
				account.setId(fellowId);
				process.getEntrustDetail().getFellowList().add(account);
			}
		}
		
		// 故障分类
		process.getEntrustDetail().getFaultCategoryList().clear();
		for (Long faultCategoryId : faultCategorys) {
			FaultCategory faultCategory = new FaultCategory();
			faultCategory.setId(faultCategoryId);
			process.getEntrustDetail().getFaultCategoryList().add(faultCategory);
		}

		entrustDetailService.save(process.getEntrustDetail());
		process.setFinishType(FinishType.FINISHED);
		process.setFinishDate(new Date());
		completeTask(taskId, () -> taskService.complete(taskId), new ArrayList<>());
		// 把解决方案写入到方案库
		sessionComponent.getCurrentUser();// 利用shiro缓存机制，防止异步任务获取session出错
		asyncDocument.insertIntoDocument(process.getEntrustDetail(), process);
	}

	@Transactional
	@Override
	@Deprecated
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
	 * @deprecated 转派已经是一个节点
	 */
	@Deprecated
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
	 * @deprecated 转派已经是一个节点
	 */
	@Deprecated
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
		public void insertIntoDocument(BaseRepairDetail detail, CustomerServiceProcess process) throws Exception {
			String problemDescription = detail.getProblemDescription();
			String failureCause = detail.getFailureCause();
			String processMode = detail.getProcessMode();

			StringBuilder sb = new StringBuilder();
			sb.append(i18nComponent.getMessage("customerService.problemDescription")).append(":")
					.append(problemDescription).append("\n");
			sb.append(i18nComponent.getMessage("customerService.failureCause")).append(":").append(failureCause)
					.append("\n");
			sb.append(i18nComponent.getMessage("customerService.processMode")).append(":").append(processMode)
					.append("\n");

			InputStream is = IOUtils.toInputStream(sb.toString(), Charset.forName("UTF-8"));

			technologyDocumentService.createTechnologyDocument(process.getName() + ".txt", is,
					((Number) sb.length()).longValue(), TypeValue.BREAKDOWNDOCUMENT.toString(),
					TypeValue.BREAKDOWNDOCUMENT.toString());
		}
	}

	@Override
	public JSONArray getDailyStartedProcess(Predicate predicate, Pageable pageable) {
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
		QCustomerServiceProcess qc = QCustomerServiceProcess.customerServiceProcess;
		// 调用mysql的DATE_FORMAT函数
		StringTemplate datePath = Expressions.stringTemplate("DATE_FORMAT({0},'{1s}')", qc.createDate,
				ConstantImpl.create("%Y-%m-%d"));

		List<Tuple> processTuples = queryFactory.select(qc.count(), datePath).from(qc).where(predicate)
				.groupBy(datePath).orderBy(datePath.desc()).limit(pageable.getPageSize()).offset(pageable.getOffset())
				.fetch();// 按照时间倒序排列
		// 由于查询结果是越新的数据越靠前，所以需要再一次倒序
		Collections.reverse(processTuples);

		JSONArray jsonArray = new JSONArray();
		for (Tuple tuple : processTuples) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("count", tuple.get(0, Long.class));
			jsonObject.put("date", tuple.get(1, String.class));
			jsonArray.add(jsonObject);
		}

		return jsonArray;
	}

}
