package com.anosi.asset.model.elasticsearch;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Setting;

@Document(indexName = "goaland", type = "accountContent")
@Setting(settingPath = "elasticsearch-analyser.json")
public class AccountContent extends BaseContent{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3421973377782292755L;

}
