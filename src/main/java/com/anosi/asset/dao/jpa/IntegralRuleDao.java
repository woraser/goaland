package com.anosi.asset.dao.jpa;

import com.anosi.asset.model.jpa.IntegralRule;

/**
 * 积分规则dao
 *
 * @ProjectName: goaland
 * @Package: com.anosi.asset.dao.jpa
 * @Description:
 * @Author: jinyao
 * @CreateDate: 2018/1/11 22:25
 * @UpdateUser: jinyao
 * @UpdateDate: 2018/1/11 22:25
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */
public interface IntegralRuleDao extends BaseJPADao<IntegralRule>{

    public IntegralRule findByKey(String key);

}
