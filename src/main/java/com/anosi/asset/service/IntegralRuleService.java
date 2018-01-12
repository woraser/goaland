package com.anosi.asset.service;

import com.anosi.asset.model.jpa.IntegralRule;

/**
 * 积分规则service
 *
 * @ProjectName: goaland
 * @Package: com.anosi.asset.service
 * @Description:
 * @Author: jinyao
 * @CreateDate: 2018/1/11 22:29
 * @UpdateUser: jinyao
 * @UpdateDate: 2018/1/11 22:29
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */
public interface IntegralRuleService extends BaseJPAService<IntegralRule>{

    public IntegralRule findByKey(String key);

}
