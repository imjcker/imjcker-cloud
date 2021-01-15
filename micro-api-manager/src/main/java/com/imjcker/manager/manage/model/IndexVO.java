package com.imjcker.manager.manage.model;

import com.imjcker.manager.manage.po.ApiCountVO;
import com.imjcker.manager.elastic.model.GroupCount;
import lombok.Data;

import java.util.List;

/**
 * @author ztzh_tanhh 2019/12/25
 **/
@Data
public class IndexVO {
    private QueryGroup group;
    private long totalCount;
    private long totalSuccess;
    private long totalFail;
    private long totalException;

    private long total;
    private List<ApiCountVO> apiCountList;
    private List<GroupCount> groupCountList;

    @Data
    public static class QueryGroup {
        private String type;
        private List<String> time;
        private List<Long> count;
    }
}
