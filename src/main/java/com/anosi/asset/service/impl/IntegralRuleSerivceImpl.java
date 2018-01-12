package com.anosi.asset.service.impl;

import com.anosi.asset.dao.jpa.BaseJPADao;
import com.anosi.asset.dao.jpa.IntegralRuleDao;
import com.anosi.asset.model.jpa.IntegralRule;
import com.anosi.asset.service.IntegralRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 积分规则service实现类
 *
 * @ProjectName: goaland
 * @Package: com.anosi.asset.service.impl
 * @Description:
 * @Author: jinyao
 * @CreateDate: 2018/1/11 22:30
 * @UpdateUser: jinyao
 * @UpdateDate: 2018/1/11 22:30
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */
@Service("integralRuleSerivce")
public class IntegralRuleSerivceImpl extends BaseJPAServiceImpl<IntegralRule> implements IntegralRuleService {

    @Autowired
    private IntegralRuleDao integralRuleDao;

    @Override
    public BaseJPADao<IntegralRule> getRepository() {
        return integralRuleDao;
    }

    @Override
    public IntegralRule findByKey(String key){
        return integralRuleDao.findByKey(key);
    }

}
