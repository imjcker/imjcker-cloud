package com.imjcker.manager.manage.po;

import java.util.ArrayList;
import java.util.List;

public class BackendRequestParamsVersionsExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public BackendRequestParamsVersionsExample() {
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

        public Criteria andVersionIdIsNull() {
            addCriterion("versionId is null");
            return (Criteria) this;
        }

        public Criteria andVersionIdIsNotNull() {
            addCriterion("versionId is not null");
            return (Criteria) this;
        }

        public Criteria andVersionIdEqualTo(String value) {
            addCriterion("versionId =", value, "versionId");
            return (Criteria) this;
        }

        public Criteria andVersionIdNotEqualTo(String value) {
            addCriterion("versionId <>", value, "versionId");
            return (Criteria) this;
        }

        public Criteria andVersionIdGreaterThan(String value) {
            addCriterion("versionId >", value, "versionId");
            return (Criteria) this;
        }

        public Criteria andVersionIdGreaterThanOrEqualTo(String value) {
            addCriterion("versionId >=", value, "versionId");
            return (Criteria) this;
        }

        public Criteria andVersionIdLessThan(String value) {
            addCriterion("versionId <", value, "versionId");
            return (Criteria) this;
        }

        public Criteria andVersionIdLessThanOrEqualTo(String value) {
            addCriterion("versionId <=", value, "versionId");
            return (Criteria) this;
        }

        public Criteria andVersionIdLike(String value) {
            addCriterion("versionId like", value, "versionId");
            return (Criteria) this;
        }

        public Criteria andVersionIdNotLike(String value) {
            addCriterion("versionId not like", value, "versionId");
            return (Criteria) this;
        }

        public Criteria andVersionIdIn(List<String> values) {
            addCriterion("versionId in", values, "versionId");
            return (Criteria) this;
        }

        public Criteria andVersionIdNotIn(List<String> values) {
            addCriterion("versionId not in", values, "versionId");
            return (Criteria) this;
        }

        public Criteria andVersionIdBetween(String value1, String value2) {
            addCriterion("versionId between", value1, value2, "versionId");
            return (Criteria) this;
        }

        public Criteria andVersionIdNotBetween(String value1, String value2) {
            addCriterion("versionId not between", value1, value2, "versionId");
            return (Criteria) this;
        }

        public Criteria andRequestParamsIdIsNull() {
            addCriterion("requestParamsId is null");
            return (Criteria) this;
        }

        public Criteria andRequestParamsIdIsNotNull() {
            addCriterion("requestParamsId is not null");
            return (Criteria) this;
        }

        public Criteria andRequestParamsIdEqualTo(Integer value) {
            addCriterion("requestParamsId =", value, "requestParamsId");
            return (Criteria) this;
        }

        public Criteria andRequestParamsIdNotEqualTo(Integer value) {
            addCriterion("requestParamsId <>", value, "requestParamsId");
            return (Criteria) this;
        }

        public Criteria andRequestParamsIdGreaterThan(Integer value) {
            addCriterion("requestParamsId >", value, "requestParamsId");
            return (Criteria) this;
        }

        public Criteria andRequestParamsIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("requestParamsId >=", value, "requestParamsId");
            return (Criteria) this;
        }

        public Criteria andRequestParamsIdLessThan(Integer value) {
            addCriterion("requestParamsId <", value, "requestParamsId");
            return (Criteria) this;
        }

        public Criteria andRequestParamsIdLessThanOrEqualTo(Integer value) {
            addCriterion("requestParamsId <=", value, "requestParamsId");
            return (Criteria) this;
        }

        public Criteria andRequestParamsIdIn(List<Integer> values) {
            addCriterion("requestParamsId in", values, "requestParamsId");
            return (Criteria) this;
        }

        public Criteria andRequestParamsIdNotIn(List<Integer> values) {
            addCriterion("requestParamsId not in", values, "requestParamsId");
            return (Criteria) this;
        }

        public Criteria andRequestParamsIdBetween(Integer value1, Integer value2) {
            addCriterion("requestParamsId between", value1, value2, "requestParamsId");
            return (Criteria) this;
        }

        public Criteria andRequestParamsIdNotBetween(Integer value1, Integer value2) {
            addCriterion("requestParamsId not between", value1, value2, "requestParamsId");
            return (Criteria) this;
        }

        public Criteria andParamsTypeIsNull() {
            addCriterion("paramsType is null");
            return (Criteria) this;
        }

        public Criteria andParamsTypeIsNotNull() {
            addCriterion("paramsType is not null");
            return (Criteria) this;
        }

        public Criteria andParamsTypeEqualTo(Integer value) {
            addCriterion("paramsType =", value, "paramsType");
            return (Criteria) this;
        }

        public Criteria andParamsTypeNotEqualTo(Integer value) {
            addCriterion("paramsType <>", value, "paramsType");
            return (Criteria) this;
        }

        public Criteria andParamsTypeGreaterThan(Integer value) {
            addCriterion("paramsType >", value, "paramsType");
            return (Criteria) this;
        }

        public Criteria andParamsTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("paramsType >=", value, "paramsType");
            return (Criteria) this;
        }

        public Criteria andParamsTypeLessThan(Integer value) {
            addCriterion("paramsType <", value, "paramsType");
            return (Criteria) this;
        }

        public Criteria andParamsTypeLessThanOrEqualTo(Integer value) {
            addCriterion("paramsType <=", value, "paramsType");
            return (Criteria) this;
        }

        public Criteria andParamsTypeIn(List<Integer> values) {
            addCriterion("paramsType in", values, "paramsType");
            return (Criteria) this;
        }

        public Criteria andParamsTypeNotIn(List<Integer> values) {
            addCriterion("paramsType not in", values, "paramsType");
            return (Criteria) this;
        }

        public Criteria andParamsTypeBetween(Integer value1, Integer value2) {
            addCriterion("paramsType between", value1, value2, "paramsType");
            return (Criteria) this;
        }

        public Criteria andParamsTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("paramsType not between", value1, value2, "paramsType");
            return (Criteria) this;
        }

        public Criteria andParamNameIsNull() {
            addCriterion("paramName is null");
            return (Criteria) this;
        }

        public Criteria andParamNameIsNotNull() {
            addCriterion("paramName is not null");
            return (Criteria) this;
        }

        public Criteria andParamNameEqualTo(String value) {
            addCriterion("paramName =", value, "paramName");
            return (Criteria) this;
        }

        public Criteria andParamNameNotEqualTo(String value) {
            addCriterion("paramName <>", value, "paramName");
            return (Criteria) this;
        }

        public Criteria andParamNameGreaterThan(String value) {
            addCriterion("paramName >", value, "paramName");
            return (Criteria) this;
        }

        public Criteria andParamNameGreaterThanOrEqualTo(String value) {
            addCriterion("paramName >=", value, "paramName");
            return (Criteria) this;
        }

        public Criteria andParamNameLessThan(String value) {
            addCriterion("paramName <", value, "paramName");
            return (Criteria) this;
        }

        public Criteria andParamNameLessThanOrEqualTo(String value) {
            addCriterion("paramName <=", value, "paramName");
            return (Criteria) this;
        }

        public Criteria andParamNameLike(String value) {
            addCriterion("paramName like", value, "paramName");
            return (Criteria) this;
        }

        public Criteria andParamNameNotLike(String value) {
            addCriterion("paramName not like", value, "paramName");
            return (Criteria) this;
        }

        public Criteria andParamNameIn(List<String> values) {
            addCriterion("paramName in", values, "paramName");
            return (Criteria) this;
        }

        public Criteria andParamNameNotIn(List<String> values) {
            addCriterion("paramName not in", values, "paramName");
            return (Criteria) this;
        }

        public Criteria andParamNameBetween(String value1, String value2) {
            addCriterion("paramName between", value1, value2, "paramName");
            return (Criteria) this;
        }

        public Criteria andParamNameNotBetween(String value1, String value2) {
            addCriterion("paramName not between", value1, value2, "paramName");
            return (Criteria) this;
        }

        public Criteria andParamValueIsNull() {
            addCriterion("paramValue is null");
            return (Criteria) this;
        }

        public Criteria andParamValueIsNotNull() {
            addCriterion("paramValue is not null");
            return (Criteria) this;
        }

        public Criteria andParamValueEqualTo(String value) {
            addCriterion("paramValue =", value, "paramValue");
            return (Criteria) this;
        }

        public Criteria andParamValueNotEqualTo(String value) {
            addCriterion("paramValue <>", value, "paramValue");
            return (Criteria) this;
        }

        public Criteria andParamValueGreaterThan(String value) {
            addCriterion("paramValue >", value, "paramValue");
            return (Criteria) this;
        }

        public Criteria andParamValueGreaterThanOrEqualTo(String value) {
            addCriterion("paramValue >=", value, "paramValue");
            return (Criteria) this;
        }

        public Criteria andParamValueLessThan(String value) {
            addCriterion("paramValue <", value, "paramValue");
            return (Criteria) this;
        }

        public Criteria andParamValueLessThanOrEqualTo(String value) {
            addCriterion("paramValue <=", value, "paramValue");
            return (Criteria) this;
        }

        public Criteria andParamValueLike(String value) {
            addCriterion("paramValue like", value, "paramValue");
            return (Criteria) this;
        }

        public Criteria andParamValueNotLike(String value) {
            addCriterion("paramValue not like", value, "paramValue");
            return (Criteria) this;
        }

        public Criteria andParamValueIn(List<String> values) {
            addCriterion("paramValue in", values, "paramValue");
            return (Criteria) this;
        }

        public Criteria andParamValueNotIn(List<String> values) {
            addCriterion("paramValue not in", values, "paramValue");
            return (Criteria) this;
        }

        public Criteria andParamValueBetween(String value1, String value2) {
            addCriterion("paramValue between", value1, value2, "paramValue");
            return (Criteria) this;
        }

        public Criteria andParamValueNotBetween(String value1, String value2) {
            addCriterion("paramValue not between", value1, value2, "paramValue");
            return (Criteria) this;
        }

        public Criteria andParamsLocationIsNull() {
            addCriterion("paramsLocation is null");
            return (Criteria) this;
        }

        public Criteria andParamsLocationIsNotNull() {
            addCriterion("paramsLocation is not null");
            return (Criteria) this;
        }

        public Criteria andParamsLocationEqualTo(Integer value) {
            addCriterion("paramsLocation =", value, "paramsLocation");
            return (Criteria) this;
        }

        public Criteria andParamsLocationNotEqualTo(Integer value) {
            addCriterion("paramsLocation <>", value, "paramsLocation");
            return (Criteria) this;
        }

        public Criteria andParamsLocationGreaterThan(Integer value) {
            addCriterion("paramsLocation >", value, "paramsLocation");
            return (Criteria) this;
        }

        public Criteria andParamsLocationGreaterThanOrEqualTo(Integer value) {
            addCriterion("paramsLocation >=", value, "paramsLocation");
            return (Criteria) this;
        }

        public Criteria andParamsLocationLessThan(Integer value) {
            addCriterion("paramsLocation <", value, "paramsLocation");
            return (Criteria) this;
        }

        public Criteria andParamsLocationLessThanOrEqualTo(Integer value) {
            addCriterion("paramsLocation <=", value, "paramsLocation");
            return (Criteria) this;
        }

        public Criteria andParamsLocationIn(List<Integer> values) {
            addCriterion("paramsLocation in", values, "paramsLocation");
            return (Criteria) this;
        }

        public Criteria andParamsLocationNotIn(List<Integer> values) {
            addCriterion("paramsLocation not in", values, "paramsLocation");
            return (Criteria) this;
        }

        public Criteria andParamsLocationBetween(Integer value1, Integer value2) {
            addCriterion("paramsLocation between", value1, value2, "paramsLocation");
            return (Criteria) this;
        }

        public Criteria andParamsLocationNotBetween(Integer value1, Integer value2) {
            addCriterion("paramsLocation not between", value1, value2, "paramsLocation");
            return (Criteria) this;
        }

        public Criteria andParamDescriptionIsNull() {
            addCriterion("paramDescription is null");
            return (Criteria) this;
        }

        public Criteria andParamDescriptionIsNotNull() {
            addCriterion("paramDescription is not null");
            return (Criteria) this;
        }

        public Criteria andParamDescriptionEqualTo(String value) {
            addCriterion("paramDescription =", value, "paramDescription");
            return (Criteria) this;
        }

        public Criteria andParamDescriptionNotEqualTo(String value) {
            addCriterion("paramDescription <>", value, "paramDescription");
            return (Criteria) this;
        }

        public Criteria andParamDescriptionGreaterThan(String value) {
            addCriterion("paramDescription >", value, "paramDescription");
            return (Criteria) this;
        }

        public Criteria andParamDescriptionGreaterThanOrEqualTo(String value) {
            addCriterion("paramDescription >=", value, "paramDescription");
            return (Criteria) this;
        }

        public Criteria andParamDescriptionLessThan(String value) {
            addCriterion("paramDescription <", value, "paramDescription");
            return (Criteria) this;
        }

        public Criteria andParamDescriptionLessThanOrEqualTo(String value) {
            addCriterion("paramDescription <=", value, "paramDescription");
            return (Criteria) this;
        }

        public Criteria andParamDescriptionLike(String value) {
            addCriterion("paramDescription like", value, "paramDescription");
            return (Criteria) this;
        }

        public Criteria andParamDescriptionNotLike(String value) {
            addCriterion("paramDescription not like", value, "paramDescription");
            return (Criteria) this;
        }

        public Criteria andParamDescriptionIn(List<String> values) {
            addCriterion("paramDescription in", values, "paramDescription");
            return (Criteria) this;
        }

        public Criteria andParamDescriptionNotIn(List<String> values) {
            addCriterion("paramDescription not in", values, "paramDescription");
            return (Criteria) this;
        }

        public Criteria andParamDescriptionBetween(String value1, String value2) {
            addCriterion("paramDescription between", value1, value2, "paramDescription");
            return (Criteria) this;
        }

        public Criteria andParamDescriptionNotBetween(String value1, String value2) {
            addCriterion("paramDescription not between", value1, value2, "paramDescription");
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
