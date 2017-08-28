package com.anosi.asset.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anosi.asset.GoalandApplication;
import com.anosi.asset.model.jpa.Department;
import com.anosi.asset.service.DepartmentService;
import com.anosi.asset.util.JsonUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = GoalandApplication.class)
@Transactional
public class TestJson {

	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private JsonUtil<Department> jsonUtil;

	@Test
	public void testListJson() throws Exception{
		Page<Department> pages = departmentService.findAll(new PageRequest(0, 10));
		String[] attributes = { "id","name","code","depGroupList-name","depGroupList-roleList-name" };
		JSONObject jsonObject = jsonUtil.parseAttributesToJson(pages, attributes);
		System.out.println(JSON.toJSONString(jsonObject));
	}

}
