package com.anosi.asset.init

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import com.anosi.asset.model.jpa.FaultCategory
import com.anosi.asset.model.jpa.QFaultCategory
import com.anosi.asset.service.FaultCategoryService

@Component
class InitFaultCategory {

	@Autowired
	private FaultCategoryService faultCategoryService
	
	protected void initFalutCategory(){
		def faultCategorys = new XmlSlurper().parse(this.getClass().getResourceAsStream("/initResources/faultCategory.xml"))
		def faultCategorysToAdd = []
		faultCategorys.faultCategory.each{faultCategory->
			def name = faultCategory.@name.toString()
			if (!faultCategoryService.exists(QFaultCategory.faultCategory.name.eq(name))) {
				faultCategorysToAdd.add(new FaultCategory(name:name))
			}
		}
		faultCategoryService.save(faultCategorysToAdd)
	}
	
}
