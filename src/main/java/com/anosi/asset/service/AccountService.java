package com.anosi.asset.service;

import com.alibaba.fastjson.JSONArray;
import com.anosi.asset.model.jpa.Account;

public interface AccountService extends BaseService<Account, Long>{
	
	public Account findByLoginId(String loginId);
	
	/***
	 * 
	 * @param account
	 * @param roles
	 * @param roleFunctionGroups
	 * @param selRolesFunctionNode
	 * @return
	 */
	public Account save(Account account, String[] roles, String[] roleFunctionGroups, String[] selRolesFunctionNode);

	/****
	 * 将权限转化成zTree
	 * @param id
	 * @return
	 */
	public JSONArray parseRoleFunctionToTree(Long id);

	/***
	 * 
	 * @param account
	 * @param password
	 * @param roles
	 * @param roleFunctionGroups
	 * @param selRolesFunctionNode
	 * @return
	 */
	public Account save(Account account, String password, String[] roles,String[] roleFunctionGroups, String[] selRolesFunctionNode);

	/***
	 * 查找上传过文档的用户
	 * @param isUploadDocument
	 * @return
	 */
	public Iterable<Account> findByIsUploadDocument(boolean isUploadDocument);
	
	/***
	 * 为account设置权限
	 * @param account
	 * @param selRolesFunctionNode
	 */
	public void resolveRoleFunction(Account account, String[] selRolesFunctionNode);
	
}
