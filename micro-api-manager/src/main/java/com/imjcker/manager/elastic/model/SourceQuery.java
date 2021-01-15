package com.imjcker.manager.elastic.model;
import lombok.Data;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * 存储日志数据到mongo，用于记录请求情况
 */
@Data
public class SourceQuery {
    private int apiId;
    private String apiName;
    private String uid;
    private  int groupId;
    private String sourceName;
    private  long startTime;
    private  long endTime;
    private String status;
    //分页
    private int pageNum;
    private int pageSize;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof SourceQuery)) return false;

        SourceQuery that = (SourceQuery) o;

        return new EqualsBuilder()
                .append(apiId, that.apiId)
                .append(groupId, that.groupId)
                .append(startTime, that.startTime)
                .append(endTime, that.endTime)
                /*.append(pageNum, that.pageNum)
                .append(pageSize, that.pageSize)*/
                .append(apiName, that.apiName)
                .append(uid, that.uid)
                .append(sourceName, that.sourceName)
                .append(status, that.status)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(apiId)
                .append(apiName)
                .append(uid)
                .append(groupId)
                .append(sourceName)
                .append(startTime)
                .append(endTime)
                .append(status)
                .append(pageNum)
                .append(pageSize)
                .toHashCode();
    }
}
