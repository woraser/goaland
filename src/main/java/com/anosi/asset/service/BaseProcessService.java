package com.anosi.asset.service;

import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.anosi.asset.model.jpa.BaseProcess;

/***
 * 所有流程最上层的接口
 * @author jinyao
 *
 * @param <T>
 */
public interface BaseProcessService<T extends BaseProcess> {
	
	/***
	 * 设置运行时属性
	 * @param t
	 * @param task
	 * @param runtimeService
	 * @return
	 */
	T setRunTimeValueForProcess(T t,Task task);
	
	/***
	 * 设置historic属性
	 * @param t
	 * @param historicTaskInstance
	 * @param historyService
	 * @return
	 */
	T setHistoricValueForProcess(T t,HistoricTaskInstance historicTaskInstance);
	
	/***
	 * 为T设置运行时属性
	 * 内部通过调用父接口的findAndSetRunTimeValue来减少重复代码
	 * @return
	 */
	T findAndSetRunTimeValue(Task task);
	
	/***
	 * 为T设置historic属性
	 * 内部通过调用父接口的findAndSetHistoricValue来减少重复代码
	 * @return
	 */
	T findAndSetHistoricValue(HistoricTaskInstance historicTaskInstance);
	
	/***
	 * 获取等待办理的任务
	 * @param pageable
	 * @return
	 */
	Page<T> findRuntimeTasks(Pageable pageable);
	
	/***
	 * 获取办理过的任务
	 * @param pageable
	 * @return
	 */
	Page<T> findHistoricTasks(Pageable pageable);
	
	/***
	 * 获取具体子类的TaskQuery
	 * @return
	 */
	TaskQuery findRuntimeTasksQuery();
	
	/***
	 * 获取具体子类的HistoricTaskInstanceQuery
	 * @return
	 */
	HistoricTaskInstanceQuery findHistoricTasksQuery();
	
	/**
	 * 根据processInstanceId查找流程
	 * @param processInstanceId
	 * @return
	 */
	T findByProcessInstanceId(String processInstanceId);
	
}
