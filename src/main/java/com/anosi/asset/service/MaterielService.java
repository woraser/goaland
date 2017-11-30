package com.anosi.asset.service;

import java.io.InputStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.anosi.asset.model.jpa.Materiel;

public interface MaterielService extends BaseJPAService<Materiel>{

	Page<Materiel> findBySearchContent(String searchContent, String deviceSN, Pageable pageable);

	void batchSave(InputStream inputStream) throws Exception;

}
