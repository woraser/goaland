package com.anosi.asset.component;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import com.anosi.asset.exception.CustomRunTimeException;
import com.anosi.asset.model.jpa.Account;

/***
 * 对account设置密码
 * 
 * @author jinyao
 *
 */
public class PasswordEncry {
	
	//随机数生成器
    private static RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();

    //指定散列算法为md5
    private static String algorithmName = "MD5";
    //散列迭代次数
    private static final int hashIterations = 2;


	/**
	 * 生成随机盐值对密码进行加密
	 * 
	 * @param account	一定要有loginId
	 * @return
	 */
	public static Account encrypt(Account account) throws Exception{
		if(StringUtils.isBlank(account.getLoginId())){
			throw new CustomRunTimeException("----------loginId is null,can not encrypt----------");
		}
		if(StringUtils.isBlank(account.getPassword())){
			throw new CustomRunTimeException("----------password is null,can not encrypt----------");
		}
		//设置salt,一个随机数的hash
		account.setSalt(randomNumberGenerator.nextBytes().toHex());
		String newPassword = new SimpleHash(algorithmName,account.getPassword(),ByteSource.Util.bytes(account.getCredentialsSalt()),hashIterations).toHex();
		account.setPassword(newPassword);
		return account;
	}

}
