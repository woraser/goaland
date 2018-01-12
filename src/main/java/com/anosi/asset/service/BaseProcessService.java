package com.anosi.asset.service;

import java.util.Date;
import java.util.List;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.anosi.asset.model.jpa.Account;
import com.anosi.asset.model.jpa.BaseProcess;
import com.anosi.asset.model.jpa.BaseProcess.FinishType;
import com.anosi.asset.model.jpa.MessageInfo;
import com.anosi.asset.model.jpa.ProcessRecord.HandleType;
import com.anosi.asset.service.impl.BaseProcessServiceImpl.DoInComplete;

/***
 * 所有流程最上层的接口
 * 
 * @author jinyao
 *
 * @param <T>
 */
public interface BaseProcessService<T extends BaseProcess> extends BaseJPAService<T> {

	/***
	 * 设置运行时属性
	 * 
	 * @param t
	 * @param task
	 * @return
	 */
	T setRunTimeValueForProcess(T t, Task task);

	/***
	 * 设置historic属性
	 * 
	 * @param t
	 * @param historicTaskInstance
	 * @return
	 */
	T setHistoricValueForProcess(T t, HistoricTaskInstance historicTaskInstance);
	
	/***
	 * 设置historic属性
	 * 
	 * @param t
	 * @param historicTaskInstance
	 * @return
	 */
	T setHistoricValueForProcess(T t);

	/***
	 * 为T设置HistoricProcessInstance 内部通过调用父接口的findAndSetRunTimeValue来减少重复代码
	 * 
	 * @param instance
	 * @return
	 */
	T findAndSetInstanceValue(HistoricProcessInstance instance);

	/***
	 * 为T设置运行时属性 内部通过调用父接口的findAndSetRunTimeValue来减少重复代码
	 * 
	 * @param task
	 * @return
	 */
	T findAndSetRunTimeValue(Task task);

	/***
	 * 为T设置historic属性 内部通过调用父接口的findAndSetHistoricValue来减少重复代码
	 * 
	 * @param historicTaskInstance
	 * @return
	 */
	T findAndSetHistoricValue(HistoricTaskInstance historicTaskInstance);

	/***
	 * 获取所有发起的流程
	 * 
	 * @param pageable
	 * @param historicProcessInstanceQuery
	 * @return
	 */
	public Page<T> findHistoricProcessInstance(Pageable pageable,
			HistoricProcessInstanceQuery historicProcessInstanceQuery);

	/***
	 * 获取等待办理的任务
	 * 
	 * @param pageable
	 * @param taskQuery
	 * @return
	 */
	Page<T> findRuntimeTasks(Pageable pageable, TaskQuery taskQuery);

	/***
	 * 获取办理过的任务
	 * 
	 * @param pageable
	 * @param historicTaskInstanceQuery
	 * @return
	 */
	Page<T> findHistoricTasks(Pageable pageable, HistoricTaskInstanceQuery historicTaskInstanceQuery);

	/***
	 * 根据taskId查找流程
	 * 
	 * @param taskId
	 * @return
	 */
	T findBytaskId(String taskId);

	/**
	 * 根据processInstanceId查找流程
	 * 
	 * @param processInstanceId
	 * @return
	 */
	T findByProcessInstanceId(String processInstanceId);
	
	/***
	 * 模板方法，可以在任务完成前后做一些固定的动作，比如生成记录和发站内信
	 * 
	 * @param taskId
	 * @param doInComplete
	 *            具体完成任务的函数
	 * @param messageInfos
	 * @throws Exception 
	 */
	void completeTask(String taskId, DoInComplete doInComplete, List<MessageInfo> messageInfos) throws Exception;

	/***
	 * 模板方法，可以在任务完成前后做一些固定的动作，比如生成记录和发站内信
	 * 
	 * @param taskId
	 * @param doInComplete
	 *            具体完成任务的函数
	 * @param messageInfos
	 * @param remain 组任务办理人
	 * @throws Exception 
	 */
	void completeTask(String taskId, DoInComplete doInComplete, List<MessageInfo> messageInfos, String remain) throws Exception;

	/***
	 * 模板方法，可以在任务完成前后做一些固定的动作，比如生成记录和发站内信
	 * 
	 * @param taskId
	 * @param doInComplete
	 * @param type
	 * @param reason
	 * @param messageInfos
	 * @param remain 组任务办理人
	 * @throws Exception 
	 */
	void completeTask(String taskId, DoInComplete doInComplete, HandleType type, String reason,
			List<MessageInfo> messageInfos) throws Exception;
	
	/***
	 * 模板方法，可以在任务完成前后做一些固定的动作，比如生成记录和发站内信
	 * 
	 * @param taskId
	 * @param doInComplete
	 * @param type
	 * @param reason
	 * @param messageInfos
	 * @param remain 组任务办理人
	 * @throws Exception 
	 */
	void completeTask(String taskId, DoInComplete doInComplete, HandleType type, String reason,
			List<MessageInfo> messageInfos, String remain) throws Exception;

	/***
	 * 获取definitionKey，应在具体子类构造方法中设置
	 * 
	 * @return
	 */
	String getDefinitionKey();

	/***
	 * save messageinfo,并向用户推送消息
	 */
	void saveMessageInfoAndSend(List<MessageInfo> messageInfos);

	/***
	 * 创建待办任务的record
	 * 
	 * @param processInstanceId
	 * @param remain 组任务办理人
	 */
	void createNewProcessRecord(String processInstanceId, String remain);

	/***
	 * 获取发起的流程，默认实现
	 * 
	 * @param pageable
	 * @param endTime
	 * @param beginTime
	 * @param timeType
	 * @param searchContent
	 * @param finishType 
	 * @return
	 */
	Page<T> findStartedProcess(Pageable pageable, String searchContent, String timeType, Date beginTime, Date endTime, FinishType finishType);

	/***
	 * 获取待办任务，默认实现
	 * 
	 * @param pageable
	 * @param endTime
	 * @param beginTime
	 * @param searchContent
	 * @return
	 */
	Page<T> findTasksToDo(Pageable pageable, String searchContent, Date beginTime, Date endTime);

	/***
	 * 获取历史任务，默认实现
	 * 
	 * @param pageable
	 * @param endTime
	 * @param beginTime
	 * @param timeType
	 * @param searchContent
	 * @param finishType 
	 * @return
	 */
	Page<T> findHistoricTasks(Pageable pageable, String searchContent, String timeType, Date beginTime, Date endTime, FinishType finishType);

	/***
	 * 获取所有发起的流程，默认实现
	 * 
	 * @param pageable
	 * @param searchContent
	 * @param timeType
	 * @param beginTime
	 * @param endTime
	 * @param finishType 
	 * @return
	 */
	Page<T> findAllProcesses(Pageable pageable, String searchContent, String timeType, Date beginTime, Date endTime, FinishType finishType);

	/***
	 * 查出下一步任务id，发给下一步代办人
	 * 
	 * @param t
	 * @param processInstanceId
	 * @param messageInfos
	 */
	public void searchNextTaskAndSend(T t, String processInstanceId, List<MessageInfo> messageInfos);

	/***
	 * 将信息发送给办理人
	 * 
	 * @param t
	 * @param taskId
	 * @param applicant
	 * @param nextAssignee
	 * @param messageInfos
	 */
	public void messageInfoForAssignee(T t, String taskId, Account nextAssignee, List<MessageInfo> messageInfos);

	/***
	 * 只发给发起人
	 * 
	 * @param t
	 * @param taskId
	 * @param applicant
	 * @param messageInfos
	 * @param reason
	 */
	public void messageInfoForApplicant(T t, String taskId, Account applicant, List<MessageInfo> messageInfos,
			String reason);

	/***
	 * 根据查询内容获取关联流程
	 * 
	 * @param searchContent
	 * @return
	 */
	public List<String> getProcessInstanceIdsBySearchContent(String searchContent);

	/***
	 * 为detail设置acitiviti数据
	 * 
	 * @param t
	 * @return
	 */
	public T getDetail(T t);

	/***
	 * 获取流程中所有不同任务的情况
	 * 
	 * @param t
	 * @return
	 */
	public List<HistoricTaskInstance> getTaskDatas(T t);

	/***
	 * 获取流程所有任务节点的key
	 * @return
	 */
	public List<String> getTaskDefinitionKeys();

}
