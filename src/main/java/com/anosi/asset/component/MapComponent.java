package com.anosi.asset.component;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.anosi.asset.model.jpa.City;
import com.anosi.asset.model.jpa.Country;
import com.anosi.asset.model.jpa.District;
import com.anosi.asset.model.jpa.Province;
import com.anosi.asset.service.CityService;
import com.anosi.asset.service.CountryService;
import com.anosi.asset.service.DistrictService;
import com.anosi.asset.service.ProvinceService;

@Component
public class MapComponent {

	@Autowired
	private CountryService countryService;
	@Autowired
	private ProvinceService provinceService;
	@Autowired
	private CityService cityService;
	@Autowired
	private DistrictService districtService;

	/***
	 * 根据jsonObject,解析出district,city,province,country,判断是否存在，不存在则插入数据库
	 * 
	 * @param jsonObject
	 *            格式:"addressComponent": { "country": "中国", "country_code": 0,
	 *            "province": "北京市", "city": "北京市", "district": "东城区", "adcode":
	 *            "110101", "street": "中华路", "street_number": "甲10号",
	 *            "direction": "西南", "distance": "64" }
	 */
	public District createMap(JSONObject jsonObject) {
		String district = jsonObject.getString("district");
		String city = jsonObject.getString("city");
		String province = jsonObject.getString("province");
		String country = jsonObject.getString("country");
		return checkDistrict(district, checkCity(city, checkProvince(province, checkCountry(country))));
	}

	private Country checkCountry(String countryName) {
		Country country = countryService.findByName(countryName);
		if (country == null) {
			country = new Country();
			country.setName(countryName);
			country = countryService.save(country);
		}
		return country;
	}

	private Province checkProvince(String provinceName, Country country) {
		Province province = provinceService.findByName(provinceName);
		if (province == null) {
			province = new Province();
			province.setName(provinceName);
			province.setCountry(country);
			province = provinceService.save(province);
		}
		return province;
	}

	private City checkCity(String cityName, Province province) {
		City city = cityService.findByName(cityName);
		if (city == null) {
			city = new City();
			city.setName(cityName);
			city.setProvince(province);
			city = cityService.save(city);
		}
		return city;
	}

	private District checkDistrict(String districtName, City city) {
		if (StringUtils.isBlank(districtName)) {
			districtName = "default";
		}
		District district = districtService.findByNameAndCity(districtName, city.getName());
		if (district == null) {
			district = new District();
			district.setName(districtName);
			district.setCity(city);
			district = districtService.save(district);
		}
		return district;
	}

}
