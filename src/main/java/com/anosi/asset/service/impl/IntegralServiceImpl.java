package com.anosi.asset.service.impl;

import com.anosi.asset.dao.jpa.BaseJPADao;
import com.anosi.asset.dao.jpa.IntegralDao;
import com.anosi.asset.model.jpa.Integral;
import com.anosi.asset.service.IntegralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 积分service实现类
 *
 * @ProjectName: goaland
 * @Package: com.anosi.asset.service.impl
 * @Description:
 * @Author: jinyao
 * @CreateDate: 2018/1/9 15:51
 * @UpdateUser: jinyao
 * @UpdateDate: 2018/1/9 15:51
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */
@Service("integralService")
@Transactional
public class IntegralServiceImpl extends BaseJPAServiceImpl<Integral> implements IntegralService {

    @Autowired
    private IntegralDao integralDao;

    @Override
    public BaseJPADao<Integral> getRepository() {
        return integralDao;
    }

}
