package com.imjcker.manager.manage.po;

public class ApiCategoryRelation {
    private Integer id;

    private Integer apiId;

    private Integer categoryId;

    public ApiCategoryRelation() {
    }

    public ApiCategoryRelation(Integer apiId, Integer categoryId) {
        this.apiId = apiId;
        this.categoryId = categoryId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getApiId() {
        return apiId;
    }

    public void setApiId(Integer apiId) {
        this.apiId = apiId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}
