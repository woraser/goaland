package com.anosi.asset.service;

import org.springframework.web.multipart.MultipartFile;

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
	 */
	void completeStartDetail(String taskId, CustomerServiceProcess process);

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
	 */
	void evaluating(String taskId, CustomerServiceProcess process);

	/***
	 * 售后服务组派单
	 * 
	 * @param taskId
	 * @param process
	 */
	void distribute(String taskId, CustomerServiceProcess process);

	/***
	 * 工程师上门维修
	 * 
	 * @param taskId
	 */
	void repair(String taskId, CustomerServiceProcess process);

	/***
	 * 委托
	 * 
	 * @param taskId
	 * @param mandatary
	 *            代理人
	 * @param process
	 */
	void entrust(String taskId, Account mandatary, String reason, CustomerServiceProcess process);

}
