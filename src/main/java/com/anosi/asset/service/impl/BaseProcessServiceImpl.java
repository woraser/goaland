package com.anosi.asset.service.impl;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.anosi.asset.component.I18nComponent;
import com.anosi.asset.component.WebSocketComponent;
import com.anosi.asset.model.jpa.BaseProcess;
import com.anosi.asset.model.jpa.MessageInfo;
import com.anosi.asset.service.BaseProcessService;
import com.anosi.asset.service.MessageInfoService;
import com.anosi.asset.util.SessionUtil;

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

	protected String definitionKey;

	protected List<MessageInfo> messageInfos;

	@Override
	public Page<T> findRuntimeTasks(Pageable pageable) {
		int firstResult = pageable.getPageNumber() * pageable.getPageSize();
		int maxResults = firstResult + pageable.getOffset();

		TaskQuery taskQuery = findRuntimeTasksQuery();

		List<Task> tasks = taskQuery.listPage(firstResult, maxResults); // 分页task
		long total = taskQuery.count(); // task总数
		logger.debug("the total for runtimeTask:{}", total);

		// lambda表达式，用task查出CustomerServiceProcess，设置相关属性后，汇聚到一个list中
		List<T> list = tasks.stream().map(task -> findAndSetRunTimeValue(task)).collect(Collectors.toList());

		return new PageImpl<>(list, pageable, total);
	}

	@Override
	public Page<T> findHistoricTasks(Pageable pageable) {
		int firstResult = pageable.getPageNumber() * pageable.getPageSize();
		int maxResults = firstResult + pageable.getOffset();

		HistoricTaskInstanceQuery historicTaskInstanceQuery = findHistoricTasksQuery();

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
	public T findAndSetRunTimeValue(Task task) {
		return setRunTimeValueForProcess(findByProcessInstanceId(task.getProcessInstanceId()), task);
	}

	@Override
	public T findAndSetHistoricValue(HistoricTaskInstance historicTaskInstance) {
		return setHistoricValueForProcess(findByProcessInstanceId(historicTaskInstance.getProcessInstanceId()),
				historicTaskInstance);
	}

	/**
	 * 返回用于查看所有运行中任务的query
	 */
	@Override
	public TaskQuery findRuntimeTasksQuery() {
		return taskService.createTaskQuery().processDefinitionKey(getDefinitionKey()).orderByTaskCreateTime().desc()
				.taskCandidateOrAssigned(SessionUtil.getCurrentUser().getLoginId());
	}

	/**
	 * 返回用于查看所有历史任务的query
	 */
	@Override
	public HistoricTaskInstanceQuery findHistoricTasksQuery() {
		return historyService.createHistoricTaskInstanceQuery().processDefinitionKey(getDefinitionKey())
				.orderByTaskCreateTime().desc().taskAssignee(SessionUtil.getCurrentUser().getLoginId());
	}

	@Override
	public void completeTask(String taskId) {
		completeTask(taskId, null);
	}

	@Override
	public void completeTask(String taskId, Map<String, Object> variables) {
		taskService.complete(taskId, variables);
		saveMessageInfoAndSend();
	}
	
	/***
	 * save messageinfo,并向用户推送消息
	 */
	protected void saveMessageInfoAndSend(){
		for (MessageInfo messageInfo : messageInfos) {
			messageInfoService.save(messageInfo);
			try {
				// 利用websocket向浏览器发送消息
				webSocketComponent.sendByQuene(messageInfo.getTo().getLoginId(),
						MessageFormat.format(i18nComponent.getMessage("messageTemplate"),
								messageInfo.getFrom().getLoginId(), messageInfo.getTitle()));
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

}
