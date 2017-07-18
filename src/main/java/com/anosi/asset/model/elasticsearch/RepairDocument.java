package com.anosi.asset.model.elasticsearch;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;


@Document(indexName="goaland",type="repairDocument",indexStoreType="fs",shards=5,replicas=1,refreshInterval="-1")
public class RepairDocument implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7259174821924357576L;
	
	private String id;

	private String problemDescription;
	
	private String failureCause;
	
	private String processMode;

	@Id
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getProblemDescription() {
		return problemDescription;
	}

	public void setProblemDescription(String problemDescription) {
		this.problemDescription = problemDescription;
	}

	public String getFailureCause() {
		return failureCause;
	}

	public void setFailureCause(String failureCause) {
		this.failureCause = failureCause;
	}

	public String getProcessMode() {
		return processMode;
	}

	public void setProcessMode(String processMode) {
		this.processMode = processMode;
	}
	
}
