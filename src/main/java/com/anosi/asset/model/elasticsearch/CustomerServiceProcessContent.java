package com.anosi.asset.model.elasticsearch;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Setting;

@Document(indexName = "goaland", type = "customerServiceProcessContent")
@Setting(settingPath = "elasticsearch-analyser.json")
public class CustomerServiceProcessContent extends BaseContent{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7903941207438027923L;

}
