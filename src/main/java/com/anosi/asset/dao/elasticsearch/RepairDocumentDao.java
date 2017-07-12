package com.anosi.asset.dao.elasticsearch;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.anosi.asset.model.elasticsearch.RepairDocument;

public interface RepairDocumentDao extends ElasticsearchRepository<RepairDocument, Long>{
	
	public List<RepairDocument> findByProblemDescriptionOrFailureCauseOrProcessMode(String problemDescription,String failureCause,String processMode);
	
}
