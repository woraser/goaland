package com.anosi.asset.service.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.anosi.asset.component.I18nComponent;
import com.anosi.asset.component.WebSocketComponent;
import com.anosi.asset.exception.CustomRunTimeException;
import com.anosi.asset.model.jpa.Account;
import com.anosi.asset.model.jpa.BaseProcess;
import com.anosi.asset.model.jpa.MessageInfo;
import com.anosi.asset.model.jpa.ProcessRecord;
import com.anosi.asset.model.jpa.ProcessRecord.HandleType;
import com.anosi.asset.service.AccountService;
import com.anosi.asset.service.BaseProcessService;
import com.anosi.asset.service.MessageInfoService;
import com.anosi.asset.service.ProcessRecordService;

/***
 * 所有流程service集成的抽象类，实现了上层接口的部分方法,剩余方法在具体流程中实现
 * <p>
 * 继承于它的子类要使用<b>多例模式@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)</b>
 * </p>
 * 
 * @author jinyao
 *
 * @param <T>
 */
@Transactional
public abstract class BaseProcessServiceImpl<T extends BaseProcess> extends BaseJPAServiceImpl<T>
		implements BaseProcessService<T> {

	private static final Logger logger = LoggerFactory.getLogger(BaseProcessServiceImpl.class);

	@Autowired
	protected TaskService taskService;
	@Autowired
	protected RuntimeService runtimeService;
	@Autowired
	protected HistoryService historyService;
	@Autowired
	protected IdentityService identityService;
	@Autowired
	protected RepositoryService repositoryService;
	@Autowired
	protected MessageInfoService messageInfoService;
	@Autowired
	protected WebSocketComponent webSocketComponent;
	@Autowired
	protected I18nComponent i18nComponent;
	@Autowired
	protected ProcessRecordService processRecordService;
	@Autowired
	protected AccountService accountService;

	protected String definitionKey;// 由于每个子类的definitionKey都是一样的，所以不会有线程安全问题

	/*--------------------------------------*/

	/*----- 设置为成员变量是因为会在多个方法中使用,这样子参数列表会清晰一些,不必每个方法后面都写几个参数,
	 * 		而且以后一旦参数变化,也不必把涉及到的方法的参数都改一遍,
	 * 
	 * 		但这样在单例模式会引起线程安全问题，所以需要多例@Scope(@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)),
	 * 		以下参数都是可选参数,都是在完成任务的时候设置的-----*/

	protected List<MessageInfo> messageInfos = new ArrayList<>();

	protected HandleType type = HandleType.PASS;// 办理的类型

	protected String reason;// 办理的理由

	protected String reamin;// 组任务的待办人

	/*--------------------------------------*/

	@Override
	public Page<T> findHistoricProcessInstance(Pageable pageable,
			HistoricProcessInstanceQuery historicProcessInstanceQuery) {
		int firstResult = pageable.getPageNumber() * pageable.getPageSize();
		int maxResults = pageable.getPageSize();

		List<HistoricProcessInstance> instances = historicProcessInstanceQuery.listPage(firstResult, maxResults);
		long total = historicProcessInstanceQuery.count(); // 总数
		logger.debug("the total for historicProcessInstance:{}", total);

		List<T> list = instances.stream().map(instance -> findAndSetInstanceValue(instance))
				.collect(Collectors.toList());

		return new PageImpl<>(list, pageable, total);
	}

	@Override
	public Page<T> findRuntimeTasks(Pageable pageable, TaskQuery taskQuery) {
		int firstResult = pageable.getPageNumber() * pageable.getPageSize();
		int maxResults = pageable.getPageSize();

		List<Task> tasks = taskQuery.listPage(firstResult, maxResults); // 分页task
		long total = taskQuery.count(); // task总数
		logger.debug("the total for runtimeTask:{}", total);

		// lambda表达式，用task查出CustomerServiceProcess，设置相关属性后，汇聚到一个list中
		List<T> list = tasks.stream().map(task -> findAndSetRunTimeValue(task)).collect(Collectors.toList());

		return new PageImpl<>(list, pageable, total);
	}

	@Override
	public Page<T> findHistoricTasks(Pageable pageable, HistoricTaskInstanceQuery historicTaskInstanceQuery) {
		int firstResult = pageable.getPageNumber() * pageable.getPageSize();
		int maxResults = pageable.getPageSize();

		List<HistoricTaskInstance> historicTaskInstances = historicTaskInstanceQuery.listPage(firstResult, maxResults);
		long total = historicTaskInstanceQuery.count(); // task总数
		logger.debug("the total for historicTaskInstance:{}", total);

		List<T> list = historicTaskInstances.stream().map(task -> findAndSetHistoricValue(task))
				.collect(Collectors.toList());

		return new PageImpl<>(list, pageable, total);
	}

	@Override
	public T setRunTimeValueForProcess(T t, Task task) {
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceId(task.getProcessInstanceId()).singleResult();
		HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
				.processInstanceId(task.getProcessInstanceId()).singleResult();
		t.setTask(task);
		t.setProcessInstance(processInstance);
		t.setHistoricProcessInstance(historicProcessInstance);
		return t;
	}

	@Override
	public T setHistoricValueForProcess(T t, HistoricTaskInstance historicTaskInstance) {
		HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
				.processInstanceId(historicTaskInstance.getProcessInstanceId()).singleResult();
		t.setHistoricProcessInstance(historicProcessInstance);
		t.setHistoricTaskInstance(historicTaskInstance);
		return t;
	}

	@Override
	public T findAndSetInstanceValue(HistoricProcessInstance instance) {
		T process = findByProcessInstanceId(instance.getId());
		process.setHistoricProcessInstance(instance);
		return process;
	}

	@Override
	public T findAndSetRunTimeValue(Task task) {
		return setRunTimeValueForProcess(findByProcessInstanceId(task.getProcessInstanceId()), task);
	}

	@Override
	public T findAndSetHistoricValue(HistoricTaskInstance historicTaskInstance) {
		return setHistoricValueForProcess(findByProcessInstanceId(historicTaskInstance.getProcessInstanceId()),
				historicTaskInstance);
	}

	@Override
	public void completeTask(String taskId, DoInComplete doInComplete) {
		T t = findBytaskId(taskId);

		// 找出这个任务的记录,设置任务的完成时间和完成类型
		ProcessRecord processRecord = processRecordService.findByTaskIdNotEnd(taskId);

		processRecord.setType(type);
		processRecord.setEndTime(new Date());
		processRecord.setReason(reason);
		processRecord.setAssignee(sessionComponent.getCurrentUser());
		String processInstanceId = processRecord.getProcessInstanceId();

		messageInfoForApplicant(t, taskId,
				accountService.findByLoginId((String) taskService.getVariable(taskId, "applicant")));// 生成发送给发起人的信息
		doInComplete.excute();// 办理任务

		// 这里只发送个人任务的信息，由于组任务相对较少，且封装复杂
		// 所以如果是组任务，将发送信息的代码自行写到DoInComplete中
		// 然后由此模板方法完成发送及创建记录
		/*-----注意以下三步的顺序，要先发送消息，再创建下一步的记录，不然不会发送--*/
		searchNextTaskAndSend(t, processInstanceId);// 生成发给下一步办理人的信息
		saveMessageInfoAndSend();// 发送
		createNewProcessRecord(processInstanceId);// 生成新的待办任务记录
	}

	@Override
	public void createNewProcessRecord(String processInstanceId) {
		List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
		for (Task task : tasks) {
			if (processRecordService.findByTaskIdNotEnd(task.getId()) == null) {
				ProcessRecord processRecord = new ProcessRecord();
				processRecord.setProcessInstanceId(processInstanceId);
				processRecord.setTaskId(task.getId());
				processRecord.setTaskName(task.getName());
				processRecord.setStartTime(new Date());
				processRecord.setType(HandleType.REAMIN_TO_DO);
				processRecord.setRemain(StringUtils.isBlank(task.getAssignee()) ? reamin : task.getAssignee());
				processRecordService.save(processRecord);
			}
		}
	}

	@Override
	public void saveMessageInfoAndSend() {
		for (MessageInfo messageInfo : messageInfos) {
			messageInfoService.save(messageInfo);
			try {
				// 利用websocket向浏览器发送消息
				webSocketComponent.sendByQuene(messageInfo.getTo().getLoginId(),
						MessageFormat.format(i18nComponent.getMessage("message.template"),
								messageInfo.getFrom().getName(), messageInfo.getTitle()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public String getDefinitionKey() {
		return Objects.requireNonNull(definitionKey, "definitionKey can not be null");
	}

	@Override
	public Page<T> findStartedProcess(Pageable pageable, String searchContent, String timeType, Date beginTime,
			Date endTime) {
		HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery()
				.processDefinitionKey(getDefinitionKey()).startedBy(sessionComponent.getCurrentUser().getLoginId())
				.orderByProcessInstanceStartTime().desc();
		if (beginTime != null) {
			if ("start".equals(timeType)) {
				historicProcessInstanceQuery.startedAfter(beginTime);
			} else if ("end".equals(timeType)) {
				historicProcessInstanceQuery.finishedAfter(endTime);
			} else {
				throw new CustomRunTimeException("timeType illegal");
			}
		}
		if (endTime != null) {
			if ("start".equals(timeType)) {
				historicProcessInstanceQuery.startedBefore(beginTime);
			} else if ("end".equals(timeType)) {
				historicProcessInstanceQuery.finishedBefore(endTime);
			} else {
				throw new CustomRunTimeException("timeType illegal");
			}
		}
		if (StringUtils.isNoneBlank(searchContent)) {
			List<String> processInstanceIdsBySearchContent = getProcessInstanceIdsBySearchContent(searchContent);
			if (!CollectionUtils.isEmpty(processInstanceIdsBySearchContent)) {
				historicProcessInstanceQuery
						.processInstanceIds(new HashSet<>(getProcessInstanceIdsBySearchContent(searchContent)));
			} else {
				// 如果查询结果为null,返回空的page
				return new PageImpl<>(new ArrayList<>(), pageable, 0);
			}
		}
		return findHistoricProcessInstance(pageable, historicProcessInstanceQuery);
	}

	@Override
	public Page<T> findTasksToDo(Pageable pageable, String searchContent, Date beginTime, Date endTime) {
		TaskQuery taskQuery = taskService.createTaskQuery().processDefinitionKey(getDefinitionKey())
				.orderByTaskCreateTime().desc().taskCandidateOrAssigned(sessionComponent.getCurrentUser().getLoginId());
		if (beginTime != null) {
			taskQuery.taskCreatedAfter(beginTime);
		}
		if (endTime != null) {
			taskQuery.taskCreatedBefore(endTime);
		}
		if (StringUtils.isNoneBlank(searchContent)) {
			List<String> processInstanceIdsBySearchContent = getProcessInstanceIdsBySearchContent(searchContent);
			if (!CollectionUtils.isEmpty(processInstanceIdsBySearchContent)) {
				taskQuery.processInstanceIdIn(getProcessInstanceIdsBySearchContent(searchContent));
			} else {
				// 如果查询结果为null,返回空的page
				return new PageImpl<>(new ArrayList<>(), pageable, 0);
			}
		}
		return findRuntimeTasks(pageable, taskQuery);
	}

	@Override
	public Page<T> findHistoricTasks(Pageable pageable, String searchContent, String timeType, Date beginTime,
			Date endTime) {
		HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery()
				.processDefinitionKey(getDefinitionKey()).orderByTaskCreateTime().desc()
				.taskAssignee(sessionComponent.getCurrentUser().getLoginId());
		if (beginTime != null) {
			if ("start".equals(timeType)) {
				historicTaskInstanceQuery.taskCreatedAfter(beginTime);
			} else if ("end".equals(timeType)) {
				historicTaskInstanceQuery.taskCompletedAfter(endTime);
			} else {
				throw new CustomRunTimeException("timeType illegal");
			}
		}
		if (endTime != null) {
			if ("start".equals(timeType)) {
				historicTaskInstanceQuery.taskCreatedBefore(beginTime);
			} else if ("end".equals(timeType)) {
				historicTaskInstanceQuery.taskCompletedBefore(endTime);
			} else {
				throw new CustomRunTimeException("timeType illegal");
			}
		}
		if (StringUtils.isNoneBlank(searchContent)) {
			List<String> processInstanceIdsBySearchContent = getProcessInstanceIdsBySearchContent(searchContent);
			if (!CollectionUtils.isEmpty(processInstanceIdsBySearchContent)) {
				historicTaskInstanceQuery.processInstanceIdIn(getProcessInstanceIdsBySearchContent(searchContent));
			} else {
				// 如果查询结果为null,返回空的page
				return new PageImpl<>(new ArrayList<>(), pageable, 0);
			}
		}
		return findHistoricTasks(pageable, historicTaskInstanceQuery);
	}

	@Override
	public Page<T> findAllProcesses(Pageable pageable, String searchContent, String timeType, Date beginTime,
			Date endTime) {
		HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery()
				.processDefinitionKey(getDefinitionKey()).orderByProcessInstanceStartTime().desc();
		if (beginTime != null) {
			if ("start".equals(timeType)) {
				historicProcessInstanceQuery.startedAfter(beginTime);
			} else if ("end".equals(timeType)) {
				historicProcessInstanceQuery.finishedAfter(endTime);
			} else {
				throw new CustomRunTimeException("timeType illegal");
			}
		}
		if (endTime != null) {
			if ("start".equals(timeType)) {
				historicProcessInstanceQuery.startedBefore(beginTime);
			} else if ("end".equals(timeType)) {
				historicProcessInstanceQuery.finishedBefore(endTime);
			} else {
				throw new CustomRunTimeException("timeType illegal");
			}
		}
		if (StringUtils.isNoneBlank(searchContent)) {
			List<String> processInstanceIdsBySearchContent = getProcessInstanceIdsBySearchContent(searchContent);
			if (!CollectionUtils.isEmpty(processInstanceIdsBySearchContent)) {
				historicProcessInstanceQuery
						.processInstanceIds(new HashSet<>(getProcessInstanceIdsBySearchContent(searchContent)));
			} else {
				// 如果查询结果为null,返回空的page
				return new PageImpl<>(new ArrayList<>(), pageable, 0);
			}
		}
		return findHistoricProcessInstance(pageable, historicProcessInstanceQuery);
	}

	@Override
	public T findBytaskId(String taskId) {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		return findByProcessInstanceId(task.getProcessInstanceId());
	}

	@Override
	public void searchNextTaskAndSend(T t, String processInstanceId) {
		List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
		for (Task task : tasks) {
			// 如果是新生成的任务
			if (processRecordService.findByTaskIdNotEnd(task.getId()) == null) {
				if (StringUtils.isNoneBlank(task.getAssignee())) {
					messageInfoForAssignee(t, task.getId(), accountService.findByLoginId(task.getAssignee()));
				}
			}
		}
	}

	@Override
	public void messageInfoForAssignee(T t, String taskId, Account nextAssignee) {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceId(task.getProcessInstanceId()).singleResult();

		MessageInfo messageInfo = new MessageInfo();
		messageInfo.setFrom(sessionComponent.getCurrentUser());
		messageInfo.setTo(nextAssignee);
		messageInfo.setSendTime(new Date());
		// 从i18n中读取信息
		messageInfo.setTitle(MessageFormat.format(i18nComponent.getMessage("message.titile.taskToDo"),
				processInstance.getProcessDefinitionName(), task.getName()));// {0}等待办理,任务:{1}
		messageInfo.setContent(MessageFormat.format(i18nComponent.getMessage("message.content.taskToDo"), t.getName(),
				task.getName()));// 流程编号为{0},任务名称为{1},等待办理

		messageInfos.add(messageInfo);
	}

	@Override
	public void messageInfoForApplicant(T t, String taskId, Account applicant) {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceId(task.getProcessInstanceId()).singleResult();

		MessageInfo messageInfo = new MessageInfo();
		messageInfo.setFrom(sessionComponent.getCurrentUser());
		messageInfo.setTo(applicant);
		messageInfo.setSendTime(new Date());
		// 从i18n中读取信息
		messageInfo.setTitle(MessageFormat.format(i18nComponent.getMessage("message.titile.taskComplete"),
				processInstance.getProcessDefinitionName()));// {0}被办理
		messageInfo.setContent(MessageFormat.format(i18nComponent.getMessage("message.content.taskComplete"),
				t.getName(), task.getName(), messageInfo.getFrom().getName(), reason));// 你发起的流程{0},{1}已经被办理,办理人为{2},办理说明:{3}

		messageInfos.add(messageInfo);
	}

	/**
	 * 流程转向操作
	 * 
	 * @param taskId
	 *            当前任务ID
	 * @param activityId
	 *            目标节点任务ID
	 * @param variables
	 *            流程变量
	 * @throws Exception
	 */
	protected void turnTransition(String taskId, String activityId, Map<String, Object> variables) throws Exception {
		// 当前节点
		ActivityImpl currActivity = findActivitiImpl(taskId, null);
		// 清空当前流向
		List<PvmTransition> oriPvmTransitionList = clearTransition(currActivity);

		// 创建新流向
		TransitionImpl newTransition = currActivity.createOutgoingTransition();
		// 目标节点
		ActivityImpl pointActivity = findActivitiImpl(taskId, activityId);
		// 设置新流向的目标节点
		newTransition.setDestination(pointActivity);

		// 执行转向任务
		taskService.complete(taskId, variables);
		// 删除目标节点新流入
		pointActivity.getIncomingTransitions().remove(newTransition);

		// 还原以前流向
		restoreTransition(currActivity, oriPvmTransitionList);
	}

	/**
	 * 根据任务ID和节点ID获取活动节点 <br>
	 * 
	 * @param taskId
	 *            任务ID
	 * @param activityId
	 *            活动节点ID <br>
	 *            如果为null或""，则默认查询当前活动节点 <br>
	 *            如果为"end"，则查询结束节点 <br>
	 * 
	 * @return
	 * @throws Exception
	 */
	private ActivityImpl findActivitiImpl(String taskId, String activityId) throws Exception {
		// 取得流程定义
		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(
						taskService.createTaskQuery().taskId(taskId).singleResult().getProcessDefinitionId());

		// 获取当前活动节点ID
		if (StringUtils.isBlank(activityId)) {
			activityId = taskService.createTaskQuery().taskId(taskId).singleResult().getTaskDefinitionKey();
		}

		// 根据流程定义，获取该流程实例的结束节点
		if (activityId.toUpperCase().equals("END")) {
			for (ActivityImpl activityImpl : processDefinition.getActivities()) {
				List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();
				if (pvmTransitionList.isEmpty()) {
					return activityImpl;
				}
			}
		}

		// 根据节点ID，获取对应的活动节点
		ActivityImpl activityImpl = ((ProcessDefinitionImpl) processDefinition).findActivity(activityId);

		return activityImpl;
	}

	/**
	 * 清空指定活动节点流向
	 * 
	 * @param activityImpl
	 *            活动节点
	 * @return 节点流向集合
	 */
	private List<PvmTransition> clearTransition(ActivityImpl activityImpl) {
		// 存储当前节点所有流向临时变量
		List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();
		// 获取当前节点所有流向，存储到临时变量，然后清空
		List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();
		for (PvmTransition pvmTransition : pvmTransitionList) {
			oriPvmTransitionList.add(pvmTransition);
		}
		pvmTransitionList.clear();

		return oriPvmTransitionList;
	}

	/**
	 * 还原指定活动节点流向
	 * 
	 * @param activityImpl
	 *            活动节点
	 * @param oriPvmTransitionList
	 *            原有节点流向集合
	 */
	private void restoreTransition(ActivityImpl activityImpl, List<PvmTransition> oriPvmTransitionList) {
		// 清空现有流向
		List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();
		pvmTransitionList.clear();
		// 还原以前流向
		for (PvmTransition pvmTransition : oriPvmTransitionList) {
			pvmTransitionList.add(pvmTransition);
		}
	}

	/***
	 * 内部方法接口,用来执行completeTask中的具体操作
	 * 
	 * @author jinyao
	 *
	 */
	@FunctionalInterface
	public interface DoInComplete {
		public void excute();
	}

}
