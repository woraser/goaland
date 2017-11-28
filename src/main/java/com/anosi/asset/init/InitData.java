package com.anosi.asset.init;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.anosi.asset.component.PasswordEncry;
import com.anosi.asset.exception.CustomRunTimeException;
import com.anosi.asset.model.jpa.Account;
import com.anosi.asset.model.jpa.DevCategory;
import com.anosi.asset.model.jpa.DevCategory.CategoryType;
import com.anosi.asset.model.jpa.DocumentType;
import com.anosi.asset.model.jpa.DocumentType.TypeValue;
import com.anosi.asset.service.AccountService;
import com.anosi.asset.service.DevCategoryService;
import com.anosi.asset.service.DocumentTypeService;
import com.anosi.asset.service.RoleService;

/***
 * 进行一些数据的初始化工作
 * 
 * @author jinyao
 *
 */
@Component
public class InitData {

	private static final Logger logger = LoggerFactory.getLogger(InitData.class);

	@Autowired
	private AccountService accountService;
	@Autowired
	private TransactionTemplate transactionTemplate;
	@Autowired
	private InitDepRelated initDepRelated;
	@Autowired
	private InitRoleFunctionRelated initRoleFunctionRelated;
	@Autowired
	private RoleService roleService;
	@Autowired
	private DevCategoryService devCategoryService;
	@Autowired
	private DocumentTypeService documentTypeService;

	@PostConstruct
	public void init() {
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {

			@Override
			protected void doInTransactionWithoutResult(TransactionStatus arg0) {
				logger.debug("init data");
				initDepRelated.initDepRelated();
				initAdmin();
				initRoleFunctionRelated.initRoleFunctionRelated();
				initDevCategory();
				initType();
			}
		});
	}

	/***
	 * 初始化admin
	 */
	private void initAdmin() {
		Account account = this.accountService.findByLoginId("admin");

		if (account == null) {
			account = new Account();
			account.setName("admin");
			account.setLoginId("admin");
			account.setPassword("123456");
			account.getRoleList().add(roleService.findByCode("admin"));
			try {
				// 设置密码
				PasswordEncry.encrypt(account);
			} catch (Exception e) {
				throw new CustomRunTimeException();
			}
			accountService.save(account);
		}
	}

	/***
	 * 初始化设备种类
	 */
	private void initDevCategory() {
		if (devCategoryService.count() == 0) {
			CategoryType[] categoryTypes = CategoryType.values();
			for (CategoryType categoryType : categoryTypes) {
				DevCategory devCategory = new DevCategory();
				devCategory.setCategoryType(categoryType);
				devCategoryService.save(devCategory);
			}
		}
	}

	/***
	 * 初始化文档类型
	 */
	private void initType() {
		if (documentTypeService.count() == 0) {
			TypeValue[] typeValues = TypeValue.values();
			for (TypeValue typeValue : typeValues) {
				DocumentType documentType = new DocumentType();
				documentType.setTypeValue(typeValue);
				documentTypeService.save(documentType);
			}
		}
	}

}
