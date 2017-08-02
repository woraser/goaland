package com.anosi.asset.service.impl;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
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

import com.anosi.asset.component.I18nComponent;
import com.anosi.asset.component.SessionUtil;
import com.anosi.asset.component.WebSocketComponent;
import com.anosi.asset.model.jpa.Account;
import com.anosi.asset.model.jpa.BaseProcess;
import com.anosi.asset.model.jpa.MessageInfo;
import com.anosi.asset.model.jpa.ProcessRecord;
import com.anosi.asset.model.jpa.ProcessRecord.HandleType;
import com.anosi.asset.service.BaseProcessService;
import com.anosi.asset.service.MessageInfoService;
import com.anosi.asset.service.ProcessRecordService;

/***
 * 所有流程service集成的抽象类，实现了上层接口的部分方法,剩余方法在具体流程中实现
 * 
 * @author jinyao
 *
 * @param <T>
 */
public abstract class BaseProcessServiceImpl<T extends BaseProcess> implements BaseProcessService<T> {

	private static final Logger logger = LoggerFactory.getLogger(BaseProcessServiceImpl.class);

	@Autowired
	protected TaskService taskService;
	@Autowired
	protected RuntimeService runtimeService;
	@Autowired
	protected HistoryService historyService;
	@Autowired
	protected MessageInfoService messageInfoService;
	@Autowired
	protected WebSocketComponent webSocketComponent;
	@Autowired
	protected I18nComponent i18nComponent;
	@Autowired
	protected ProcessRecordService processRecordService;

	protected String definitionKey;

	protected List<MessageInfo> messageInfos;

	/*-----设置为成员变量是因为会在多个方法中使用,这样子参数列表会清晰一些-----*/

	protected HandleType type = HandleType.PASS;// 办理的类型

	protected String reason;// 办理的理由

	protected String reamin;// 组任务的待办人

	/*--------------------------------------*/

	@Override
	public Page<T> findHistoricProcessInstance(Pageable pageable,
			HistoricProcessInstanceQuery historicProcessInstanceQuery) {
		int firstResult = pageable.getPageNumber() * pageable.getPageSize();
		int maxResults = firstResult + pageable.getOffset();

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
		int maxResults = firstResult + pageable.getOffset();

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
		int maxResults = firstResult + pageable.getOffset();

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
		t.setTask(task);
		t.setProcessInstance(processInstance);
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
	public void completeTask(String taskId, T t, Account applicant, DoInComplete doInComplete) {
		completeTask(taskId, t, applicant, null, doInComplete);
	}

	@Override
	public void completeTask(String taskId, T t, Account applicant, Account nextAssignee, DoInComplete doInComplete) {
		// 找出这个任务的记录,设置任务的完成时间和完成类型
		ProcessRecord processRecord = processRecordService.findByTaskId(taskId);

		processRecord.setType(type);
		processRecord.setEndTime(new Date());
		processRecord.setReason(reason);
		processRecord.setAssignee(SessionUtil.getCurrentUser());
		String processInstanceId = processRecord.getProcessInstanceId();

		messageInfoForApplicant(t, taskId, applicant);// 发送给发起人
		doInComplete.excute();// 办理任务

		if (nextAssignee == null) {
			saveMessageInfoAndSend();// 发送
		} else {
			/*-----注意以下三步的顺序，要先发送消息，再创建下一步的记录，不然不会发送--*/
			searchNextTaskAndSend(t, processInstanceId, nextAssignee);// 发给下一步办理人
			saveMessageInfoAndSend();// 发送
			createNewProcessRecord(processInstanceId);// 生成新的待办任务记录
		}
	}

	@Override
	public void createNewProcessRecord(String processInstanceId) {
		List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
		for (Task task : tasks) {
			if (processRecordService.findByTaskId(task.getId()) == null) {
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public String getDefinitionKey() {
		return Objects.requireNonNull(definitionKey, "definitionKey can not be null");
	}

	@Override
	public Page<T> findStartedProcess(Pageable pageable) {
		HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery()
				.processDefinitionKey(getDefinitionKey()).startedBy(SessionUtil.getCurrentUser().getLoginId())
				.orderByProcessInstanceStartTime().desc();
		return findHistoricProcessInstance(pageable, historicProcessInstanceQuery);
	}

	@Override
	public Page<T> findTasksToDo(Pageable pageable) {
		TaskQuery taskQuery = taskService.createTaskQuery().processDefinitionKey(getDefinitionKey())
				.orderByTaskCreateTime().desc().taskCandidateOrAssigned(SessionUtil.getCurrentUser().getLoginId());
		return findRuntimeTasks(pageable, taskQuery);
	}

	@Override
	public Page<T> findHistoricTasks(Pageable pageable) {
		HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery()
				.processDefinitionKey(getDefinitionKey()).orderByTaskCreateTime().desc()
				.taskAssignee(SessionUtil.getCurrentUser().getLoginId());
		return findHistoricTasks(pageable, historicTaskInstanceQuery);
	}

	@Override
	public T findBytaskId(String taskId) {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		return findByProcessInstanceId(task.getProcessInstanceId());
	}

	@Override
	public void searchNextTaskAndSend(T t, String processInstanceId, Account nextAssignee) {
		List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
		for (Task task : tasks) {
			// 如果是新生成的任务
			if (processRecordService.findByTaskId(task.getId()) == null) {
				messageInfoForAssignee(t, task.getId(), nextAssignee);
			}
		}
	}

	@Override
	public void messageInfoForAssignee(T t, String taskId, Account nextAssignee) {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceId(task.getProcessInstanceId()).singleResult();

		MessageInfo messageInfo = new MessageInfo();
		messageInfo.setFrom(SessionUtil.getCurrentUser());
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
		messageInfo.setFrom(SessionUtil.getCurrentUser());
		messageInfo.setTo(applicant);
		messageInfo.setSendTime(new Date());
		// 从i18n中读取信息
		messageInfo.setTitle(MessageFormat.format(i18nComponent.getMessage("message.titile.taskComplete"),
				processInstance.getProcessDefinitionName()));// {0}被办理
		messageInfo.setContent(MessageFormat.format(i18nComponent.getMessage("message.content.taskComplete"),
				t.getName(), task.getName(), messageInfo.getFrom().getName()));// 你发起的流程{0},{1}已经被办理,办理人为{2}

		messageInfos.add(messageInfo);
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
