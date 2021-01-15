package com.imjcker.repository;

import com.imjcker.domain.ApiInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiRepository extends JpaRepository<ApiInfo, Integer> {
}
