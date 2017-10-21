package com.anosi.asset.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anosi.asset.dao.elasticsearch.BaseContentDao;
import com.anosi.asset.dao.elasticsearch.CustomerServiceProcessContentDao;
import com.anosi.asset.model.elasticsearch.CustomerServiceProcessContent;
import com.anosi.asset.model.jpa.CustomerServiceProcess;
import com.anosi.asset.service.CustomerServiceProcessContentService;

@Transactional
@Service("customerServiceProcessContentService")
public class CustomerServiceProcessContentServiceImpl
		extends BaseContentServiceImpl<CustomerServiceProcessContent, String, CustomerServiceProcess>
		implements CustomerServiceProcessContentService {

	@Autowired
	private CustomerServiceProcessContentDao customerServiceProcessContentDao;

	@Override
	public BaseContentDao<CustomerServiceProcessContent, String> getRepository() {
		return customerServiceProcessContentDao;
	}

	@Override
	public CustomerServiceProcessContent saveContent(CustomerServiceProcess customerServiceProcess) throws Exception {
		String id = String.valueOf(customerServiceProcess.getId());
		CustomerServiceProcessContent customerServiceProcessContent = customerServiceProcessContentDao.findOne(id);
		if (customerServiceProcessContent == null) {
			customerServiceProcessContent = new CustomerServiceProcessContent();
			customerServiceProcessContent.setId(id);
		}
		customerServiceProcessContent = setCommonContent(customerServiceProcessContent, customerServiceProcess);
		return customerServiceProcessContentDao.save(customerServiceProcessContent);
	}

	@Override
	public <S extends CustomerServiceProcess> Iterable<CustomerServiceProcessContent> saveContent(Iterable<S> obs)
			throws Exception {
		List<CustomerServiceProcessContent> customerServiceProcessContents = new ArrayList<>();
		for (CustomerServiceProcess customerServiceProcess : obs) {
			String id = String.valueOf(customerServiceProcess.getId());
			CustomerServiceProcessContent customerServiceProcessContent = customerServiceProcessContentDao.findOne(id);
			if (customerServiceProcessContent == null) {
				customerServiceProcessContent = new CustomerServiceProcessContent();
				customerServiceProcessContent.setId(id);
			}
			customerServiceProcessContent = setCommonContent(customerServiceProcessContent, customerServiceProcess);
			customerServiceProcessContents.add(customerServiceProcessContent);
		}
		return customerServiceProcessContentDao.save(customerServiceProcessContents);
	}

}
