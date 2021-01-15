package com.imjcker.manager.manage.po;

import java.util.ArrayList;
import java.util.List;

public class ApiResultEtlExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ApiResultEtlExample() {
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

        public Criteria andApiIdIsNull() {
            addCriterion("apiId is null");
            return (Criteria) this;
        }

        public Criteria andApiIdIsNotNull() {
            addCriterion("apiId is not null");
            return (Criteria) this;
        }

        public Criteria andApiIdEqualTo(Integer value) {
            addCriterion("apiId =", value, "apiId");
            return (Criteria) this;
        }

        public Criteria andApiIdNotEqualTo(Integer value) {
            addCriterion("apiId <>", value, "apiId");
            return (Criteria) this;
        }

        public Criteria andApiIdGreaterThan(Integer value) {
            addCriterion("apiId >", value, "apiId");
            return (Criteria) this;
        }

        public Criteria andApiIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("apiId >=", value, "apiId");
            return (Criteria) this;
        }

        public Criteria andApiIdLessThan(Integer value) {
            addCriterion("apiId <", value, "apiId");
            return (Criteria) this;
        }

        public Criteria andApiIdLessThanOrEqualTo(Integer value) {
            addCriterion("apiId <=", value, "apiId");
            return (Criteria) this;
        }

        public Criteria andApiIdIn(List<Integer> values) {
            addCriterion("apiId in", values, "apiId");
            return (Criteria) this;
        }

        public Criteria andApiIdNotIn(List<Integer> values) {
            addCriterion("apiId not in", values, "apiId");
            return (Criteria) this;
        }

        public Criteria andApiIdBetween(Integer value1, Integer value2) {
            addCriterion("apiId between", value1, value2, "apiId");
            return (Criteria) this;
        }

        public Criteria andApiIdNotBetween(Integer value1, Integer value2) {
            addCriterion("apiId not between", value1, value2, "apiId");
            return (Criteria) this;
        }

        public Criteria andDisIdIsNull() {
            addCriterion("disId is null");
            return (Criteria) this;
        }

        public Criteria andDisIdIsNotNull() {
            addCriterion("disId is not null");
            return (Criteria) this;
        }

        public Criteria andDisIdEqualTo(Integer value) {
            addCriterion("disId =", value, "disId");
            return (Criteria) this;
        }

        public Criteria andDisIdNotEqualTo(Integer value) {
            addCriterion("disId <>", value, "disId");
            return (Criteria) this;
        }

        public Criteria andDisIdGreaterThan(Integer value) {
            addCriterion("disId >", value, "disId");
            return (Criteria) this;
        }

        public Criteria andDisIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("disId >=", value, "disId");
            return (Criteria) this;
        }

        public Criteria andDisIdLessThan(Integer value) {
            addCriterion("disId <", value, "disId");
            return (Criteria) this;
        }

        public Criteria andDisIdLessThanOrEqualTo(Integer value) {
            addCriterion("disId <=", value, "disId");
            return (Criteria) this;
        }

        public Criteria andDisIdIn(List<Integer> values) {
            addCriterion("disId in", values, "disId");
            return (Criteria) this;
        }

        public Criteria andDisIdNotIn(List<Integer> values) {
            addCriterion("disId not in", values, "disId");
            return (Criteria) this;
        }

        public Criteria andDisIdBetween(Integer value1, Integer value2) {
            addCriterion("disId between", value1, value2, "disId");
            return (Criteria) this;
        }

        public Criteria andDisIdNotBetween(Integer value1, Integer value2) {
            addCriterion("disId not between", value1, value2, "disId");
            return (Criteria) this;
        }

        public Criteria andEKeyIsNull() {
            addCriterion("eKey is null");
            return (Criteria) this;
        }

        public Criteria andEKeyIsNotNull() {
            addCriterion("eKey is not null");
            return (Criteria) this;
        }

        public Criteria andEKeyEqualTo(String value) {
            addCriterion("eKey =", value, "eKey");
            return (Criteria) this;
        }

        public Criteria andEKeyNotEqualTo(String value) {
            addCriterion("eKey <>", value, "eKey");
            return (Criteria) this;
        }

        public Criteria andEKeyGreaterThan(String value) {
            addCriterion("eKey >", value, "eKey");
            return (Criteria) this;
        }

        public Criteria andEKeyGreaterThanOrEqualTo(String value) {
            addCriterion("eKey >=", value, "eKey");
            return (Criteria) this;
        }

        public Criteria andEKeyLessThan(String value) {
            addCriterion("eKey <", value, "eKey");
            return (Criteria) this;
        }

        public Criteria andEKeyLessThanOrEqualTo(String value) {
            addCriterion("eKey <=", value, "eKey");
            return (Criteria) this;
        }

        public Criteria andEKeyLike(String value) {
            addCriterion("eKey like", value, "eKey");
            return (Criteria) this;
        }

        public Criteria andEKeyNotLike(String value) {
            addCriterion("eKey not like", value, "eKey");
            return (Criteria) this;
        }

        public Criteria andEKeyIn(List<String> values) {
            addCriterion("eKey in", values, "eKey");
            return (Criteria) this;
        }

        public Criteria andEKeyNotIn(List<String> values) {
            addCriterion("eKey not in", values, "eKey");
            return (Criteria) this;
        }

        public Criteria andEKeyBetween(String value1, String value2) {
            addCriterion("eKey between", value1, value2, "eKey");
            return (Criteria) this;
        }

        public Criteria andEKeyNotBetween(String value1, String value2) {
            addCriterion("eKey not between", value1, value2, "eKey");
            return (Criteria) this;
        }

        public Criteria andTKeyIsNull() {
            addCriterion("tKey is null");
            return (Criteria) this;
        }

        public Criteria andTKeyIsNotNull() {
            addCriterion("tKey is not null");
            return (Criteria) this;
        }

        public Criteria andTKeyEqualTo(String value) {
            addCriterion("tKey =", value, "tKey");
            return (Criteria) this;
        }

        public Criteria andTKeyNotEqualTo(String value) {
            addCriterion("tKey <>", value, "tKey");
            return (Criteria) this;
        }

        public Criteria andTKeyGreaterThan(String value) {
            addCriterion("tKey >", value, "tKey");
            return (Criteria) this;
        }

        public Criteria andTKeyGreaterThanOrEqualTo(String value) {
            addCriterion("tKey >=", value, "tKey");
            return (Criteria) this;
        }

        public Criteria andTKeyLessThan(String value) {
            addCriterion("tKey <", value, "tKey");
            return (Criteria) this;
        }

        public Criteria andTKeyLessThanOrEqualTo(String value) {
            addCriterion("tKey <=", value, "tKey");
            return (Criteria) this;
        }

        public Criteria andTKeyLike(String value) {
            addCriterion("tKey like", value, "tKey");
            return (Criteria) this;
        }

        public Criteria andTKeyNotLike(String value) {
            addCriterion("tKey not like", value, "tKey");
            return (Criteria) this;
        }

        public Criteria andTKeyIn(List<String> values) {
            addCriterion("tKey in", values, "tKey");
            return (Criteria) this;
        }

        public Criteria andTKeyNotIn(List<String> values) {
            addCriterion("tKey not in", values, "tKey");
            return (Criteria) this;
        }

        public Criteria andTKeyBetween(String value1, String value2) {
            addCriterion("tKey between", value1, value2, "tKey");
            return (Criteria) this;
        }

        public Criteria andTKeyNotBetween(String value1, String value2) {
            addCriterion("tKey not between", value1, value2, "tKey");
            return (Criteria) this;
        }

        public Criteria andTValueIsNull() {
            addCriterion("tValue is null");
            return (Criteria) this;
        }

        public Criteria andTValueIsNotNull() {
            addCriterion("tValue is not null");
            return (Criteria) this;
        }

        public Criteria andTValueEqualTo(String value) {
            addCriterion("tValue =", value, "tValue");
            return (Criteria) this;
        }

        public Criteria andTValueNotEqualTo(String value) {
            addCriterion("tValue <>", value, "tValue");
            return (Criteria) this;
        }

        public Criteria andTValueGreaterThan(String value) {
            addCriterion("tValue >", value, "tValue");
            return (Criteria) this;
        }

        public Criteria andTValueGreaterThanOrEqualTo(String value) {
            addCriterion("tValue >=", value, "tValue");
            return (Criteria) this;
        }

        public Criteria andTValueLessThan(String value) {
            addCriterion("tValue <", value, "tValue");
            return (Criteria) this;
        }

        public Criteria andTValueLessThanOrEqualTo(String value) {
            addCriterion("tValue <=", value, "tValue");
            return (Criteria) this;
        }

        public Criteria andTValueLike(String value) {
            addCriterion("tValue like", value, "tValue");
            return (Criteria) this;
        }

        public Criteria andTValueNotLike(String value) {
            addCriterion("tValue not like", value, "tValue");
            return (Criteria) this;
        }

        public Criteria andTValueIn(List<String> values) {
            addCriterion("tValue in", values, "tValue");
            return (Criteria) this;
        }

        public Criteria andTValueNotIn(List<String> values) {
            addCriterion("tValue not in", values, "tValue");
            return (Criteria) this;
        }

        public Criteria andTValueBetween(String value1, String value2) {
            addCriterion("tValue between", value1, value2, "tValue");
            return (Criteria) this;
        }

        public Criteria andTValueNotBetween(String value1, String value2) {
            addCriterion("tValue not between", value1, value2, "tValue");
            return (Criteria) this;
        }

        public Criteria andDescriptionIsNull() {
            addCriterion("description is null");
            return (Criteria) this;
        }

        public Criteria andDescriptionIsNotNull() {
            addCriterion("description is not null");
            return (Criteria) this;
        }

        public Criteria andDescriptionEqualTo(String value) {
            addCriterion("description =", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotEqualTo(String value) {
            addCriterion("description <>", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionGreaterThan(String value) {
            addCriterion("description >", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionGreaterThanOrEqualTo(String value) {
            addCriterion("description >=", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionLessThan(String value) {
            addCriterion("description <", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionLessThanOrEqualTo(String value) {
            addCriterion("description <=", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionLike(String value) {
            addCriterion("description like", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotLike(String value) {
            addCriterion("description not like", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionIn(List<String> values) {
            addCriterion("description in", values, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotIn(List<String> values) {
            addCriterion("description not in", values, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionBetween(String value1, String value2) {
            addCriterion("description between", value1, value2, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotBetween(String value1, String value2) {
            addCriterion("description not between", value1, value2, "description");
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
