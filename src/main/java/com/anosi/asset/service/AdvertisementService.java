package com.anosi.asset.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.anosi.asset.model.jpa.Advertisement;

public interface AdvertisementService extends BaseJPAService<Advertisement>{

	Page<Advertisement> findByContentSearch(String searchContent, Pageable pageable);

}
