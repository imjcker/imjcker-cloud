package com.imjcker.manager.manage.model;

import com.imjcker.manager.elastic.model.ApiIdCount;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ztzh_tanhh 2020/1/15
 **/
@Data
public class CountApiByAppKeyVO {
    private ShowVO.QueryGroup groupCount;
    private long totalCount;
    private long totalSuccess;
    private long totalFail;

    private long total;//表格总条数,用于分页
    private List<ApiIdCount> apiCountList = new ArrayList<>();

    @Data
    public static class QueryGroup {
        private List<String> time;
        private List<Long> count;
    }
}
