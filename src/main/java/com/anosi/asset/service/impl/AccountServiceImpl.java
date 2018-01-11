package com.anosi.asset.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.anosi.asset.model.jpa.*;
import com.anosi.asset.service.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.anosi.asset.component.PasswordEncry;
import com.anosi.asset.component.RemoteComponent;
import com.anosi.asset.dao.jpa.AccountDao;
import com.anosi.asset.dao.jpa.BaseJPADao;
import com.anosi.asset.exception.CustomRunTimeException;
import com.anosi.asset.util.URLConncetUtil;

@Service("accountService")
@Transactional
public class AccountServiceImpl extends BaseJPAServiceImpl<Account> implements AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    private AccountDao accountDao;
    @Autowired
    private RoleFunctionService roleFunctionService;
    @Autowired
    private PrivilegeService privilegeService;
    @Autowired
    private RoleFunctionBtnService roleFunctionBtnService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleFunctionGroupService roleFunctionGroupService;
    @Autowired
    private RemoteComponent remoteComponent;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private IntegralService integralService;

    @Override
    public BaseJPADao<Account> getRepository() {
        return accountDao;
    }

    @Override
    public Account findByLoginId(String loginId) {
        logger.debug("findByLoginId:{}", loginId);
        return this.accountDao.findByLoginId(loginId);
    }

    @Override
    public Account save(Account account, String password, String[] roles, String[] roleFunctionGroups,
                        String[] selRolesFunctionNode) throws Exception {
        // 如果是新添加用户，在iotx系统中远程添加
        if (account.getId() == null) {
            String result = URLConncetUtil.sendPostString(remoteComponent.getFullPath("/account/save/remote"),
                    "loginId=" + account.getLoginId() + "&password=" + password, remoteComponent.getHearders());
            JSONObject jsonObject = JSON.parseObject(result);
            if (!Objects.equals(jsonObject.getString("result"), "success")) {
                throw new CustomRunTimeException(jsonObject.toString());
            }
        }
        if (StringUtils.isNoneBlank(password)) {
            account.setPassword(password);
            // 设置密码
            PasswordEncry.encrypt(account);
            account = accountDao.save(account);
        }

        // 创建积分
        if (account.getIntegral() == null) {
            Integral integral = new Integral();
            integral.setAccount(account);
            integralService.save(integral);
        }

        account.getRoleList().clear();
        if (roles != null && roles.length != 0) {
            for (String role : roles) {
                account.getRoleList().add(roleService.getOne(Long.parseLong(role)));
            }
        }

        account.getRoleFunctionGroupList().clear();
        if (roleFunctionGroups != null && roleFunctionGroups.length != 0) {
            for (String group : roleFunctionGroups) {
                account.getRoleFunctionGroupList().add(roleFunctionGroupService.getOne(Long.parseLong(group)));
            }
        }

        privilegeService.deleteByAccountLoginId(account.getLoginId());
        if (selRolesFunctionNode != null && selRolesFunctionNode.length != 0) {
            resolveRoleFunction(account, selRolesFunctionNode);
        }
        return save(account);
    }

    @Override
    public void resolveRoleFunction(Account account, String[] selRolesFunctionNode) {
        for (String node : selRolesFunctionNode) {
            if (node.contains("menu_")) {
                long roleFunctionId = Long.parseLong(node.replace("menu_", ""));
                RoleFunction roleFunction = roleFunctionService.getOne(roleFunctionId);
                // 判断之前是否已经有这个权限了
                Privilege privilege = privilegeService.findByAccountAndRoleFunction(account.getLoginId(),
                        roleFunction.getRoleFunctionPageId());
                if (privilege == null) {
                    privilege = new Privilege();
                    privilege.setAccount(account);
                    privilege.setRoleFunction(roleFunction);
                    privilegeService.save(privilege);
                }
            } else {
                RoleFunctionBtn btn = roleFunctionBtnService.getOne(Long.parseLong(node));
                Privilege privilege = privilegeService.findByAccountAndRoleFunction(account.getLoginId(),
                        btn.getRoleFunction().getRoleFunctionPageId());
                if (privilege != null) {
                    // 判断之前是否已有这个按钮权限
                    if (!privilege.getRoleFunctionBtnList().contains(btn)) {
                        privilege.getRoleFunctionBtnList().add(btn);
                    }
                } else {
                    privilege = new Privilege();
                    privilege.setAccount(account);
                    privilege.setRoleFunction(btn.getRoleFunction());
                    privilege.getRoleFunctionBtnList().add(btn);
                    privilegeService.save(privilege);
                }
            }
        }
    }

    @Override
    public JSONArray parseRoleFunctionToTree(Long id) {
        List<RoleFunction> roleFunctions = new ArrayList<>();
        List<RoleFunctionBtn> roleFunctionBtns = new ArrayList<>();
        if (id != null) {
            Account account = accountDao.getOne(id);
            List<Privilege> privilegeList = account.getPrivilegeList();
            // 获取用户已经有的权限
            roleFunctions = privilegeList.stream().map(Privilege::getRoleFunction).collect(Collectors.toList());
            roleFunctionBtns = privilegeList.stream().map(Privilege::getRoleFunctionBtnList).collect(
                    () -> new ArrayList<RoleFunctionBtn>(), (list, item) -> list.addAll(item),
                    (list1, list2) -> list1.addAll(list2));
        }
        return roleFunctionService.parseToTree(roleFunctions, roleFunctionBtns);
    }

    @Override
    public Iterable<Account> findByIsUploadDocument(boolean isUploadDocument) {
        return accountDao.findByUploadDocument(isUploadDocument);
    }

    @Override
    public Page<Account> findByContentSearch(String content, Pageable pageable) {
        logger.debug("page:{},size:{}", pageable.getPageNumber(), pageable.getPageSize());
        return accountDao.findBySearchContent(entityManager, content, pageable);
    }

}
