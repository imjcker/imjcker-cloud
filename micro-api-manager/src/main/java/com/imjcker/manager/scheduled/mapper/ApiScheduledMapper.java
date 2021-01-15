package com.imjcker.manager.scheduled.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author WT
 * @Date 16:04 2019/8/1
 * @Version ${version}
 */
@Component
@Mapper
public interface ApiScheduledMapper {

    List<Map<String,String>> findApiAddr();
}
