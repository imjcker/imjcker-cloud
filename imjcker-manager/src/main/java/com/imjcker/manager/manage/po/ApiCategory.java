package com.imjcker.manager.manage.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@ApiModel(value = "ApiCategory", description = "领域分类对象")
public class ApiCategory {
    @ApiModelProperty(value = "id", name = "ID", example = "1, 2, 3...", notes = "删除,更新时必填")
    private Integer id;

    @ApiModelProperty(value = "name", name = "名称", example = "金融科技", notes = "新增时必填")
    private String name;

    @ApiModelProperty(value = "description", name = "分类描述", example = "金融科技类接口分类", notes = "新增时必填")
    private String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
