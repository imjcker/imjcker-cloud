package com.imjcker.manager.health.repository;

import com.imjcker.manager.health.model.Instance;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author ztzh_tanhh 2019/11/29
 **/
public interface InstanceRepository extends JpaRepository<Instance, String> {
}
