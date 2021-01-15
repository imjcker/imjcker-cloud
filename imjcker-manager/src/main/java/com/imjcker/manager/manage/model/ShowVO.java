package com.imjcker.manager.manage.model;

import com.imjcker.manager.elastic.model.AppKeyCount;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ztzh_tanhh 2019/12/25
 **/
@Data
public class ShowVO {
    private QueryGroup groupCount;
    private long totalCount;
    private long totalSuccess;
    private long totalFail;

    private long total;//表格总条数,用于分页
    private List<AppKeyCount> appKeyCountList = new ArrayList<>();

    @Data
    public static class QueryGroup {
        private List<String> time;
        private List<Long> count;
    }
}
