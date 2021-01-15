package com.imjcker.manager.manage.service;

import com.imjcker.manager.manage.po.ApiCombination;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ApiCombinationService {

    List<ApiCombination> selectById(Integer combinationId);

    Integer insert(ApiCombination apiCombination);

    Integer update(ApiCombination apiCombination);

    Integer delete(ApiCombination apiCombination);
}
