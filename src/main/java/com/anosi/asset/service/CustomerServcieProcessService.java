package com.anosi.asset.service;

import org.springframework.web.multipart.MultipartFile;

import com.anosi.asset.model.jpa.Account;
import com.anosi.asset.model.jpa.CustomerServiceProcess;
import com.anosi.asset.model.jpa.CustomerServiceProcess.EvaluatingDetail;
import com.anosi.asset.model.jpa.CustomerServiceProcess.ExamineDetail;
import com.anosi.asset.model.jpa.CustomerServiceProcess.RepairDetail;
import com.anosi.asset.model.jpa.CustomerServiceProcess.StartDetail;

public interface CustomerServcieProcessService extends BaseProcessService<CustomerServiceProcess>{

	/***
	 * 销售部/工程部/质检部发起
	 * 流程变量:${assignee}:发起人
	 * @param startDetail 
	 * @param account 
	 * @param multipartFiles 
	 */
	void startProcess(Account account, StartDetail startDetail, MultipartFile[] multipartFiles) throws Exception;
	
	/**
	 * 发起人完成发起的清单
	 * @param engineeDep 流程变量,下一步工程部办理人${engineeDep}
	 * @param taskId
	 * @param startDetail 存放发起流程表单信息的bean
	 */
	void completeStartDetail(Account engineeDep,String taskId,StartDetail startDetail);
	
	/****
	 * 领导审批
	 * @param engineeDep
	 * @param taskId
	 * @param examineDetail
	 */
	void examine(Account engineeDep, String taskId, ExamineDetail examineDetail);
	
	/***
	 * 工程部问题评估
	 * @param servicer 流程变量:${servicer}:下一步售后服务组办理人
	 * @param taskId
	 */
	void evaluating(Account servicer,String taskId,EvaluatingDetail evaluatingDetail);
	
	/***
	 * 售后服务组派单
	 * @param engineer 流程变量:${engineer}:下一步工程师
	 * @param taskId
	 */
	void distribute(Account engineer,String taskId);
	
	/***
	 * 工程师上门维修
	 * @param taskId
	 */
	void repair(String taskId,RepairDetail repairDetail);
	
	/***
	 * 委托
	 * @param taskId
	 * @param mandatary 代理人
	 */
	void entrust(String taskId,Account mandatary,String reason);

}
