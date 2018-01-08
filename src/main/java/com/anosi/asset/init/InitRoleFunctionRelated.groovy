package com.anosi.asset.init

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import com.anosi.asset.model.jpa.Account
import com.anosi.asset.model.jpa.Privilege
import com.anosi.asset.model.jpa.RoleFunction
import com.anosi.asset.model.jpa.RoleFunctionBtn
import com.anosi.asset.service.AccountService
import com.anosi.asset.service.PrivilegeService
import com.anosi.asset.service.RoleFunctionBtnService
import com.anosi.asset.service.RoleFunctionService

/***
 * 初始化菜单和按钮
 * 因为涉及xml操作，所以用groovy来写比较优雅
 * @author jinyao
 *
 */
@Component
class InitRoleFunctionRelated {

	private static final Logger logger = LoggerFactory.getLogger(InitRoleFunctionRelated.class);

	@Autowired
	private PrivilegeService privilegeService;
	@Autowired
	private RoleFunctionService roleFunctionService
	@Autowired
	private RoleFunctionBtnService roleFunctionBtnService
	@Autowired
	private AccountService accountService;

	private Account account

	protected void initRoleFunctionRelated(){
		def roleFunctions = new XmlSlurper().parse(this.getClass().getResourceAsStream("/initResources/roleFunctionRelated.xml"))
		account = this.accountService.findByLoginId("admin");
		// 闭包
		roleFunctions.roleFunction.each{roleFunction->
			//判断是否有子权限
			checkSubRoleFunction(roleFunction,null)
		}
	}

	/***
	 * 判断是否存在子权限，内部使用递归
	 * @param roleFunction
	 * @return
	 */
	private void checkSubRoleFunction(Object roleFunction,RoleFunction parentRoleFunction){
		// TODO 加入url
		def realRoleFunction = checkRoleFunction(roleFunction.@roleFunctionPageId.toString(),roleFunction.@name.toString(),parentRoleFunction)
		logger.debug("roleFunction:{}",realRoleFunction.roleFunctionPageId)
		// 初始化按钮
		roleFunction.roleFunctionBtn.each{
			checkRoleFunctionBtn(it.@btnId.toString(), realRoleFunction,it.@name.toString())
		}
		roleFunction.roleFunction.each{
			//递归
			checkSubRoleFunction(it,realRoleFunction)
		}
	}

	/**
	 * 判断roleFunction是否存在
	 * @param roleFunctionPageId
	 * @return
	 */
	private RoleFunction checkRoleFunction(String roleFunctionPageId,String name,RoleFunction parentRoleFunction){
		def roleFunction = roleFunctionService.findByRoleFunctionPageId(roleFunctionPageId)
		if(roleFunction==null){
			roleFunction = new RoleFunction(roleFunctionPageId:roleFunctionPageId,name:name,parentRoleFunction:parentRoleFunction)
			roleFunction = roleFunctionService.save(roleFunction)
			//为admin添加权限
			def privilege = new Privilege();
			privilege.setRoleFunction(roleFunction);
			privilege.setAccount(account);
			privilegeService.save(privilege);
		}
		return roleFunction
	}

	/***
	 * 判断roleFunctionBtn是否存在
	 * @param btnId
	 * @param roleFunction
	 * @return
	 */
	private RoleFunctionBtn checkRoleFunctionBtn(String btnId,RoleFunction roleFunction,String name){
		def roleFunctionBtn = roleFunctionBtnService.findByBtnIdAndRoleFunction(btnId, roleFunction.roleFunctionPageId)
		if(roleFunctionBtn==null){
			roleFunctionBtn = new RoleFunctionBtn(btnId:btnId,name:name,roleFunction:roleFunction)
			roleFunctionBtn = roleFunctionBtnService.save(roleFunctionBtn)
			//为admin添加权限
			def privilege = this.privilegeService.findByAccountAndRoleFunction(account.loginId, roleFunction.roleFunctionPageId);
			if(privilege!=null){
				privilege.getRoleFunctionBtnList().add(roleFunctionBtn);
			}
		}
		return roleFunctionBtn
	}

}
