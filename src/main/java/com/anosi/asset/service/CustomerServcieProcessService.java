package com.anosi.asset.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.anosi.asset.model.jpa.Account;
import com.anosi.asset.model.jpa.CustomerServiceProcess;

public interface CustomerServcieProcessService extends BaseProcessService<CustomerServiceProcess> {

	/***
	 * 销售部/工程部/质检部发起 流程变量:${assignee}:发起人
	 * 
	 * @param process
	 * @param multipartFiles
	 */
	void startProcess(CustomerServiceProcess process, MultipartFile[] multipartFiles) throws Exception;

	/**
	 * 发起人完成发起的清单
	 * 
	 * @param taskId
	 * @param process
	 * @throws Exception
	 */
	void completeStartDetail(String taskId, CustomerServiceProcess process) throws Exception;

	/****
	 * 领导审批
	 * 
	 * @param taskId
	 * @param process
	 * @throws Exception
	 */
	void examine(String taskId, CustomerServiceProcess process) throws Exception;

	/***
	 * 工程部问题评估
	 * 
	 * @param taskId
	 * @throws Exception
	 */
	void evaluating(String taskId, CustomerServiceProcess process) throws Exception;

	/***
	 * 售后服务组派单
	 * 
	 * @param taskId
	 * @param process
	 * @throws Exception
	 */
	void distribute(String taskId, CustomerServiceProcess process) throws Exception;

	/***
	 * 工程师上门维修
	 * 
	 * @param taskId
	 * @throws Exception
	 */
	void repair(String taskId, CustomerServiceProcess process) throws Exception;

	/***
	 * 委托
	 * 
	 * @param taskId
	 * @param mandatary
	 *            代理人
	 * @param process
	 */
	void entrust(String taskId, Account mandatary, String reason, CustomerServiceProcess process);

	/***
	 * 获取任务的分布
	 * 
	 * @return
	 */
	JSONArray getTaskDistribute();

	/***
	 * 根据种类和流程实例id进行count
	 * 
	 * @param id
	 * @param processInstanceIds
	 * @return
	 */
	Long countByDevCategoryAndInstanceId(Long id, List<String> processInstanceIds);

	/***
	 * 根据种类和流程实例id进行查询
	 * 
	 * @param id
	 * @param processInstanceIds
	 * @return
	 */
	List<CustomerServiceProcess> findByDevCategoryAndInstanceId(Long id, List<String> processInstanceIds);

}
