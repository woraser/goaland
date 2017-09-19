package com.anosi.asset.init

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import com.anosi.asset.model.jpa.City
import com.anosi.asset.model.jpa.District
import com.anosi.asset.model.jpa.Province
import com.anosi.asset.service.CityService
import com.anosi.asset.service.DistrictService
import com.anosi.asset.service.ProvinceService

/****
 * 省市区县的初始化
 * @author jinyao
 *
 */
@Component
class InitCityRelated {

	@Autowired
	private ProvinceService provinceService
	@Autowired
	private CityService cityService
	@Autowired
	private DistrictService districtService
	
	/***
	 * 初始化省份
	 */
	protected void initProvince(){
		//由于省市区县数据很少改动，所以这里只判断count是否为0
		if (provinceService.count() == 0){
			def provinceList = []
			def provinces = new XmlSlurper().parse(this.getClass().getResourceAsStream("/initResources/Provinces.xml"))
			provinces.Province.each{
				def province = new Province(pid:it.@ID.toString(),name:it.@ProvinceName.toString())
				provinceList.add(province)
			}
			provinceService.save(provinceList)
		}
	}
	
	/***
	 * 初始化城市
	 */
	protected void initCity(){
		if (cityService.count() == 0){
			def cityList = []
			def citys = new XmlSlurper().parse(this.getClass().getResourceAsStream("/initResources/Cities.xml"))
			citys.City.each{
				def province = provinceService.findByPID(it.@PID.toString())
				def city = new City(cid:it.@ID.toString(),name:it.@CityName.toString(),province:province)
				cityList.add(city)
			}
			cityService.save(cityList)
		}
	}
	
	/****
	 * 初始化地区
	 */
	protected void initDistrict(){
		if (districtService.count() == 0){
			def districtList = []
			def districts = new XmlSlurper().parse(this.getClass().getResourceAsStream("/initResources/Districts.xml"))
			districts.District.each{
				def city = cityService.findByCID(it.@CID.toString())
				def province = new District(name:it.@DistrictName.toString(),city:city)
				districtList.add(province)
			}
			districtService.save(districtList)
		}
	}
	
}
