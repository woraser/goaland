package com.anosi.asset.dao.jpa;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.anosi.asset.model.jpa.RepairedDeviceDailyPer;

public interface RepairedDeviceDailyPerDao extends BaseJPADao<RepairedDeviceDailyPer> {

	public List<RepairedDeviceDailyPer> findByDevCategoryIdEquals(Long devCategoryId, Pageable pageable);

}
