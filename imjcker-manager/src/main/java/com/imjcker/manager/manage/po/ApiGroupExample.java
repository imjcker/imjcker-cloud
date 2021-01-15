package com.imjcker.manager.manage.po;

import java.util.ArrayList;
import java.util.List;

public class ApiGroupExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ApiGroupExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andGroupUUIDIsNull() {
            addCriterion("groupUUID is null");
            return (Criteria) this;
        }

        public Criteria andGroupUUIDIsNotNull() {
            addCriterion("groupUUID is not null");
            return (Criteria) this;
        }

        public Criteria andGroupUUIDEqualTo(String value) {
            addCriterion("groupUUID =", value, "groupUUID");
            return (Criteria) this;
        }

        public Criteria andGroupUUIDNotEqualTo(String value) {
            addCriterion("groupUUID <>", value, "groupUUID");
            return (Criteria) this;
        }

        public Criteria andGroupUUIDGreaterThan(String value) {
            addCriterion("groupUUID >", value, "groupUUID");
            return (Criteria) this;
        }

        public Criteria andGroupUUIDGreaterThanOrEqualTo(String value) {
            addCriterion("groupUUID >=", value, "groupUUID");
            return (Criteria) this;
        }

        public Criteria andGroupUUIDLessThan(String value) {
            addCriterion("groupUUID <", value, "groupUUID");
            return (Criteria) this;
        }

        public Criteria andGroupUUIDLessThanOrEqualTo(String value) {
            addCriterion("groupUUID <=", value, "groupUUID");
            return (Criteria) this;
        }

        public Criteria andGroupUUIDLike(String value) {
            addCriterion("groupUUID like", value, "groupUUID");
            return (Criteria) this;
        }

        public Criteria andGroupUUIDNotLike(String value) {
            addCriterion("groupUUID not like", value, "groupUUID");
            return (Criteria) this;
        }

        public Criteria andGroupUUIDIn(List<String> values) {
            addCriterion("groupUUID in", values, "groupUUID");
            return (Criteria) this;
        }

        public Criteria andGroupUUIDNotIn(List<String> values) {
            addCriterion("groupUUID not in", values, "groupUUID");
            return (Criteria) this;
        }

        public Criteria andGroupUUIDBetween(String value1, String value2) {
            addCriterion("groupUUID between", value1, value2, "groupUUID");
            return (Criteria) this;
        }

        public Criteria andGroupUUIDNotBetween(String value1, String value2) {
            addCriterion("groupUUID not between", value1, value2, "groupUUID");
            return (Criteria) this;
        }

        public Criteria andGroupNameIsNull() {
            addCriterion("groupName is null");
            return (Criteria) this;
        }

        public Criteria andGroupNameIsNotNull() {
            addCriterion("groupName is not null");
            return (Criteria) this;
        }

        public Criteria andGroupNameEqualTo(String value) {
            addCriterion("groupName =", value, "groupName");
            return (Criteria) this;
        }

        public Criteria andGroupNameNotEqualTo(String value) {
            addCriterion("groupName <>", value, "groupName");
            return (Criteria) this;
        }

        public Criteria andGroupNameGreaterThan(String value) {
            addCriterion("groupName >", value, "groupName");
            return (Criteria) this;
        }

        public Criteria andGroupNameGreaterThanOrEqualTo(String value) {
            addCriterion("groupName >=", value, "groupName");
            return (Criteria) this;
        }

        public Criteria andGroupNameLessThan(String value) {
            addCriterion("groupName <", value, "groupName");
            return (Criteria) this;
        }

        public Criteria andGroupNameLessThanOrEqualTo(String value) {
            addCriterion("groupName <=", value, "groupName");
            return (Criteria) this;
        }

        public Criteria andGroupNameLike(String value) {
            addCriterion("groupName like", value, "groupName");
            return (Criteria) this;
        }

        public Criteria andGroupNameNotLike(String value) {
            addCriterion("groupName not like", value, "groupName");
            return (Criteria) this;
        }

        public Criteria andGroupNameIn(List<String> values) {
            addCriterion("groupName in", values, "groupName");
            return (Criteria) this;
        }

        public Criteria andGroupNameNotIn(List<String> values) {
            addCriterion("groupName not in", values, "groupName");
            return (Criteria) this;
        }

        public Criteria andGroupNameBetween(String value1, String value2) {
            addCriterion("groupName between", value1, value2, "groupName");
            return (Criteria) this;
        }

        public Criteria andGroupNameNotBetween(String value1, String value2) {
            addCriterion("groupName not between", value1, value2, "groupName");
            return (Criteria) this;
        }

        public Criteria andGroupDomainNameIsNull() {
            addCriterion("groupDomainName is null");
            return (Criteria) this;
        }

        public Criteria andGroupDomainNameIsNotNull() {
            addCriterion("groupDomainName is not null");
            return (Criteria) this;
        }

        public Criteria andGroupDomainNameEqualTo(String value) {
            addCriterion("groupDomainName =", value, "groupDomainName");
            return (Criteria) this;
        }

        public Criteria andGroupDomainNameNotEqualTo(String value) {
            addCriterion("groupDomainName <>", value, "groupDomainName");
            return (Criteria) this;
        }

        public Criteria andGroupDomainNameGreaterThan(String value) {
            addCriterion("groupDomainName >", value, "groupDomainName");
            return (Criteria) this;
        }

        public Criteria andGroupDomainNameGreaterThanOrEqualTo(String value) {
            addCriterion("groupDomainName >=", value, "groupDomainName");
            return (Criteria) this;
        }

        public Criteria andGroupDomainNameLessThan(String value) {
            addCriterion("groupDomainName <", value, "groupDomainName");
            return (Criteria) this;
        }

        public Criteria andGroupDomainNameLessThanOrEqualTo(String value) {
            addCriterion("groupDomainName <=", value, "groupDomainName");
            return (Criteria) this;
        }

        public Criteria andGroupDomainNameLike(String value) {
            addCriterion("groupDomainName like", value, "groupDomainName");
            return (Criteria) this;
        }

        public Criteria andGroupDomainNameNotLike(String value) {
            addCriterion("groupDomainName not like", value, "groupDomainName");
            return (Criteria) this;
        }

        public Criteria andGroupDomainNameIn(List<String> values) {
            addCriterion("groupDomainName in", values, "groupDomainName");
            return (Criteria) this;
        }

        public Criteria andGroupDomainNameNotIn(List<String> values) {
            addCriterion("groupDomainName not in", values, "groupDomainName");
            return (Criteria) this;
        }

        public Criteria andGroupDomainNameBetween(String value1, String value2) {
            addCriterion("groupDomainName between", value1, value2, "groupDomainName");
            return (Criteria) this;
        }

        public Criteria andGroupDomainNameNotBetween(String value1, String value2) {
            addCriterion("groupDomainName not between", value1, value2, "groupDomainName");
            return (Criteria) this;
        }

        public Criteria andGroupDescriptionIsNull() {
            addCriterion("groupDescription is null");
            return (Criteria) this;
        }

        public Criteria andGroupDescriptionIsNotNull() {
            addCriterion("groupDescription is not null");
            return (Criteria) this;
        }

        public Criteria andGroupDescriptionEqualTo(String value) {
            addCriterion("groupDescription =", value, "groupDescription");
            return (Criteria) this;
        }

        public Criteria andGroupDescriptionNotEqualTo(String value) {
            addCriterion("groupDescription <>", value, "groupDescription");
            return (Criteria) this;
        }

        public Criteria andGroupDescriptionGreaterThan(String value) {
            addCriterion("groupDescription >", value, "groupDescription");
            return (Criteria) this;
        }

        public Criteria andGroupDescriptionGreaterThanOrEqualTo(String value) {
            addCriterion("groupDescription >=", value, "groupDescription");
            return (Criteria) this;
        }

        public Criteria andGroupDescriptionLessThan(String value) {
            addCriterion("groupDescription <", value, "groupDescription");
            return (Criteria) this;
        }

        public Criteria andGroupDescriptionLessThanOrEqualTo(String value) {
            addCriterion("groupDescription <=", value, "groupDescription");
            return (Criteria) this;
        }

        public Criteria andGroupDescriptionLike(String value) {
            addCriterion("groupDescription like", value, "groupDescription");
            return (Criteria) this;
        }

        public Criteria andGroupDescriptionNotLike(String value) {
            addCriterion("groupDescription not like", value, "groupDescription");
            return (Criteria) this;
        }

        public Criteria andGroupDescriptionIn(List<String> values) {
            addCriterion("groupDescription in", values, "groupDescription");
            return (Criteria) this;
        }

        public Criteria andGroupDescriptionNotIn(List<String> values) {
            addCriterion("groupDescription not in", values, "groupDescription");
            return (Criteria) this;
        }

        public Criteria andGroupDescriptionBetween(String value1, String value2) {
            addCriterion("groupDescription between", value1, value2, "groupDescription");
            return (Criteria) this;
        }

        public Criteria andGroupDescriptionNotBetween(String value1, String value2) {
            addCriterion("groupDescription not between", value1, value2, "groupDescription");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Integer value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Integer value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Integer value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Integer value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Integer value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Integer> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Integer> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Integer value1, Integer value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("createTime is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("createTime is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Long value) {
            addCriterion("createTime =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Long value) {
            addCriterion("createTime <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Long value) {
            addCriterion("createTime >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Long value) {
            addCriterion("createTime >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Long value) {
            addCriterion("createTime <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Long value) {
            addCriterion("createTime <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Long> values) {
            addCriterion("createTime in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Long> values) {
            addCriterion("createTime not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Long value1, Long value2) {
            addCriterion("createTime between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Long value1, Long value2) {
            addCriterion("createTime not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("updateTime is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("updateTime is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Long value) {
            addCriterion("updateTime =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Long value) {
            addCriterion("updateTime <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Long value) {
            addCriterion("updateTime >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Long value) {
            addCriterion("updateTime >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Long value) {
            addCriterion("updateTime <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Long value) {
            addCriterion("updateTime <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Long> values) {
            addCriterion("updateTime in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Long> values) {
            addCriterion("updateTime not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Long value1, Long value2) {
            addCriterion("updateTime between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Long value1, Long value2) {
            addCriterion("updateTime not between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andGroupLevelIsNull() {
            addCriterion("groupLevel is null");
            return (Criteria) this;
        }

        public Criteria andGroupLevelIsNotNull() {
            addCriterion("groupLevel is not null");
            return (Criteria) this;
        }

        public Criteria andGroupLevelEqualTo(Integer value) {
            addCriterion("groupLevel =", value, "groupLevel");
            return (Criteria) this;
        }

        public Criteria andGroupLevelNotEqualTo(Integer value) {
            addCriterion("groupLevel <>", value, "groupLevel");
            return (Criteria) this;
        }

        public Criteria andGroupLevelGreaterThan(Integer value) {
            addCriterion("groupLevel >", value, "groupLevel");
            return (Criteria) this;
        }

        public Criteria andGroupLevelGreaterThanOrEqualTo(Integer value) {
            addCriterion("groupLevel >=", value, "groupLevel");
            return (Criteria) this;
        }

        public Criteria andGroupLevelLessThan(Integer value) {
            addCriterion("groupLevel <", value, "groupLevel");
            return (Criteria) this;
        }

        public Criteria andGroupLevelLessThanOrEqualTo(Integer value) {
            addCriterion("groupLevel <=", value, "groupLevel");
            return (Criteria) this;
        }

        public Criteria andGroupLevelIn(List<Integer> values) {
            addCriterion("groupLevel in", values, "groupLevel");
            return (Criteria) this;
        }

        public Criteria andGroupLevelNotIn(List<Integer> values) {
            addCriterion("groupLevel not in", values, "groupLevel");
            return (Criteria) this;
        }

        public Criteria andGroupLevelBetween(Integer value1, Integer value2) {
            addCriterion("groupLevel between", value1, value2, "groupLevel");
            return (Criteria) this;
        }

        public Criteria andGroupLevelNotBetween(Integer value1, Integer value2) {
            addCriterion("groupLevel not between", value1, value2, "groupLevel");
            return (Criteria) this;
        }

        public Criteria andParentIdIsNull() {
            addCriterion("parentId is null");
            return (Criteria) this;
        }

        public Criteria andParentIdIsNotNull() {
            addCriterion("parentId is not null");
            return (Criteria) this;
        }

        public Criteria andParentIdEqualTo(Integer value) {
            addCriterion("parentId =", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdNotEqualTo(Integer value) {
            addCriterion("parentId <>", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdGreaterThan(Integer value) {
            addCriterion("parentId >", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("parentId >=", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdLessThan(Integer value) {
            addCriterion("parentId <", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdLessThanOrEqualTo(Integer value) {
            addCriterion("parentId <=", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdIn(List<Integer> values) {
            addCriterion("parentId in", values, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdNotIn(List<Integer> values) {
            addCriterion("parentId not in", values, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdBetween(Integer value1, Integer value2) {
            addCriterion("parentId between", value1, value2, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdNotBetween(Integer value1, Integer value2) {
            addCriterion("parentId not between", value1, value2, "parentId");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}
