package com.imjcker.manager.manage.po;

import lombok.Data;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

@Data
public class CallBackParam {
    private Integer id;

    private Integer apiId;

    private Integer resultCodeId;

    private String paramName;

    private String paramType;

    private String description;

    private String position;

    private Integer parentId;

    private String remark;

    private String value;

    private List<CallBackParam> children = new ArrayList<>();

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        CallBackParam that = (CallBackParam) o;

        return new EqualsBuilder()
                .append(apiId, that.apiId)
                .append(paramName, that.paramName)
                .append(paramType, that.paramType)
                .append(position, that.position)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(apiId)
                .append(paramName)
                .append(paramType)
                .append(position)
                .toHashCode();
    }
}
