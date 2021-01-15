package com.imjcker.manager.manage.vo;

import com.imjcker.manager.manage.po.ApiCategory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "ApiCategoryQuery", description = "领域分类页面对象", parent = ApiCategory.class)
public class ApiCategoryQuery extends ApiCategory {
    @ApiModelProperty(value = "pageNum", name = "pageNum", example = "1, 2, 3...")
    private Integer pageNum;
    @ApiModelProperty(value = "pageSize", name = "pageSize", example = "10, 20, 50...")
    private Integer pageSize;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
