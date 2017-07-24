package com.anosi.asset.shiro;

import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import com.anosi.asset.model.jpa.Account;
import com.anosi.asset.model.jpa.Privilege;
import com.anosi.asset.model.jpa.RoleFunction;
import com.anosi.asset.model.jpa.RoleFunctionBtn;
import com.anosi.asset.service.AccountService;

/***
 * 自定义realm
 * @author jinyao
 *
 */
public class CustomRealm extends AuthorizingRealm{

	private static final Logger logger = LoggerFactory.getLogger(CustomRealm.class);
	
	@Autowired
	@Lazy
	//lazy是为了开启spring cache的时候，不让redis和shiro的encache冲突，因为shiro初始化得早
	//特么被坑了一天。。。
	private AccountService accountService;

	/**
     * 此方法调用  hasRole,hasPermission的时候才会进行回调.
     *
     * 权限信息.(授权):
     * 1、如果用户正常退出，缓存自动清空；
     * 2、如果用户非正常退出，缓存自动清空；
     * 3、如果我们修改了用户的权限，而用户不退出系统，修改的权限无法立即生效。
     * （需要手动编程进行实现；放在service进行调用）
     * 在权限修改后调用realm中的方法，realm已经由spring管理，所以从spring中获取realm实例，
     * 调用clearCached方法；
     * :Authorization 是授权访问控制，用于对用户进行的操作授权，证明该用户是否允许进行当前操作，如访问某个链接，某个资源文件等。
     * @param principals
     * @return
     */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
		logger.info("##################执行Shiro权限认证##################");
		//获取当前登录输入的用户名，等价于(String) principalCollection.fromRealm(getName()).iterator().next();
        String loginName = (String)super.getAvailablePrincipal(principalCollection); 
        //到数据库查是否有此对象
        Account account = accountService.findByLoginId(loginName);// 实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
        if(account!=null){
        	SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
        	//用户的角色
        	info.addRole(account.getRole().getCode());
        	//设置用户权限
            List<Privilege> privilegeList = account.getPrivilegeList();
            for (Privilege privilege : privilegeList) {
				RoleFunction roleFunction = privilege.getRoleFunction();
				List<RoleFunctionBtn> roleFunctionBtnList = privilege.getRoleFunctionBtnList();
				//默认添加view权限
				info.addStringPermission(roleFunction.getRoleFunctionPageId()+":view");
				//添加详细的权限
				for (RoleFunctionBtn roleFunctionBtn : roleFunctionBtnList) {
					info.addStringPermission(roleFunction.getRoleFunctionPageId()+":"+roleFunctionBtn.getBtnId());
				}
			}
        	return info;
        }
		return null;
	}

	/**
     * 认证信息.(身份验证)
     * Authentication 是用来验证用户身份
     * @param token
     * @return
     * @throws AuthenticationException
     */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
		//UsernamePasswordToken对象用来存放提交的登录信息
        UsernamePasswordToken token=(UsernamePasswordToken) authenticationToken;
        
        logger.info("验证当前Subject时获取到token为：" + ReflectionToStringBuilder.toString(token, ToStringStyle.MULTI_LINE_STYLE)); 
        
        Account account = accountService.findByLoginId(token.getUsername());
        if(account!=null){
            //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配
            return new SimpleAuthenticationInfo(account.getLoginId(), account.getPassword(),ByteSource.Util.bytes(account.getCredentialsSalt()),getName());
        }
		return null;
	}

	
}
