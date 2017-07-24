package com.anosi.asset.service.impl;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.anosi.asset.service.BaseService;

@Service("baseService")
@Transactional
public class BaseServiceImpl<T> implements BaseService<T,Long>{

}
