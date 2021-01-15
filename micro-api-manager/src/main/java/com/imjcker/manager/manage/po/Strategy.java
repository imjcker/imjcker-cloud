package com.imjcker.manager.manage.po;

import com.imjcker.manager.manage.annotation.BusinessId;
import com.imjcker.manager.manage.annotation.ConditionParam;

import javax.persistence.Column;
import javax.persistence.Table;

/**限流策略*/
@Table(name = "current_limit_strategy")
public class Strategy {
    @ConditionParam
    @BusinessId
    @Column
    private Integer id;
    @Column
    private String uuid;
    @Column
    private String name;
    @Column
    private Integer unit;
    @Column
    private Integer no;
    @Column
    private Integer status;
    @Column
    private Long createTime;
    @Column
    private Long updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid == null ? null : uuid.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getUnit() {
        return unit;
    }

    public void setUnit(Integer unit) {
        this.unit = unit;
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

}
