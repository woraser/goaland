package com.anosi.asset.model.elasticsearch;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Setting;

@Document(indexName = "goaland", type = "deviceContent")
@Setting(settingPath = "elasticsearch-analyser.json")
public class DeviceContent extends BaseContent{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7104907189005860177L;

}
