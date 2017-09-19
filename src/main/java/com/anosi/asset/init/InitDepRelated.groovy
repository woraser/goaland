package com.anosi.asset.init

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import com.anosi.asset.model.jpa.DepGroup
import com.anosi.asset.model.jpa.Department
import com.anosi.asset.model.jpa.Role
import com.anosi.asset.service.DepGroupService
import com.anosi.asset.service.DepartmentService
import com.anosi.asset.service.RoleService

/***
 * 初始化部门，部门内部组，角色
 * 因为涉及xml操作，所以用groovy来写比较优雅
 * @author jinyao
 *
 */
@Component
class InitDepRelated {

	@Autowired
	private DepartmentService departmentService
	@Autowired
	private DepGroupService depGroupService
	@Autowired
	private RoleService roleService

	protected void initDepRelated(){
		def departments = new XmlSlurper().parse(this.getClass().getResourceAsStream("/initResources/depRelated.xml"))
		// 初始化前，需要判断是否在数据库中存在
		// 闭包
		departments.department.each{department->
			def realDep=checkDepartment(department.@code.toString(), department.@name.toString())
			// 闭包
			department.depGroup.each{depGroup->
				def realDepGroup=checkDepGroup(depGroup.@code.toString(), depGroup.@name.toString(),realDep)
				// 闭包
				depGroup.role.each{role->
					checkRole(role.@code.toString(), role.@name.toString(),realDepGroup)
				}
			}
		}
	}

	/**
	 * 判断department是否存在
	 * @param code
	 * @param name
	 * @return
	 */
	private Department checkDepartment(String code,String name){
		def department = departmentService.findByCode(code)
		if(department==null){
			department = new Department(code:code,name:name)
			department = departmentService.save(department)
		}
		return department
	}

	/***
	 * 判断depGroup是否存在
	 * @param code
	 * @param name
	 * @param department
	 * @return
	 */
	private DepGroup checkDepGroup(String code,String name,Department department){
		def depGroup = depGroupService.findByCode(code)
		if(depGroup==null){
			depGroup = new DepGroup(code:code,name:name,department:department)
			depGroup = depGroupService.save(depGroup)
		}
		return depGroup
	}

	/***
	 * 判断role是否存在
	 * @param code
	 * @param name
	 * @param depGroup
	 * @return
	 */
	private Role checkRole(String code,String name,DepGroup depGroup){
		def role = roleService.findByCode(code)
		if(role==null){
			role = new Role(code:code,name:name,depGroup:depGroup)
			role = roleService.save(role)
		}
		return role
	}

}
