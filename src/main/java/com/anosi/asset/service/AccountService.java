package com.anosi.asset.service;

import com.alibaba.fastjson.JSONArray;
import com.anosi.asset.model.jpa.Account;

public interface AccountService extends BaseService<Account, Long>{
	
	public Account findByLoginId(String loginId);
	
	/***
	 * 
	 * @param account
	 * @param password
	 * @param selRolesFunctionNode 
	 * @return
	 */
	public Account save(Account account, String password, String[] selRolesFunctionNode);

	/****
	 * 将权限转化成zTree
	 * @param id
	 * @return
	 */
	public JSONArray parseRoleFunctionToTree(Long id);

	/***
	 * 
	 * @param account
	 * @param selRolesFunctionNode
	 * @return
	 */
	public Account save(Account account, String[] selRolesFunctionNode);

	/***
	 * 查找上传过文档的用户
	 * @param isUploadDocument
	 * @return
	 */
	public Iterable<Account> findByIsUploadDocument(boolean isUploadDocument);
	
}
