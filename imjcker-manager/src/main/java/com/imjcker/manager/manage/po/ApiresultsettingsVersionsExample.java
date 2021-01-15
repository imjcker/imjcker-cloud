package com.imjcker.manager.manage.po;

import java.util.ArrayList;
import java.util.List;

public class ApiresultsettingsVersionsExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ApiresultsettingsVersionsExample() {
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

        public Criteria andErrorCodeIsNull() {
            addCriterion("errorCode is null");
            return (Criteria) this;
        }

        public Criteria andErrorCodeIsNotNull() {
            addCriterion("errorCode is not null");
            return (Criteria) this;
        }

        public Criteria andErrorCodeEqualTo(Integer value) {
            addCriterion("errorCode =", value, "errorCode");
            return (Criteria) this;
        }

        public Criteria andErrorCodeNotEqualTo(Integer value) {
            addCriterion("errorCode <>", value, "errorCode");
            return (Criteria) this;
        }

        public Criteria andErrorCodeGreaterThan(Integer value) {
            addCriterion("errorCode >", value, "errorCode");
            return (Criteria) this;
        }

        public Criteria andErrorCodeGreaterThanOrEqualTo(Integer value) {
            addCriterion("errorCode >=", value, "errorCode");
            return (Criteria) this;
        }

        public Criteria andErrorCodeLessThan(Integer value) {
            addCriterion("errorCode <", value, "errorCode");
            return (Criteria) this;
        }

        public Criteria andErrorCodeLessThanOrEqualTo(Integer value) {
            addCriterion("errorCode <=", value, "errorCode");
            return (Criteria) this;
        }

        public Criteria andErrorCodeIn(List<Integer> values) {
            addCriterion("errorCode in", values, "errorCode");
            return (Criteria) this;
        }

        public Criteria andErrorCodeNotIn(List<Integer> values) {
            addCriterion("errorCode not in", values, "errorCode");
            return (Criteria) this;
        }

        public Criteria andErrorCodeBetween(Integer value1, Integer value2) {
            addCriterion("errorCode between", value1, value2, "errorCode");
            return (Criteria) this;
        }

        public Criteria andErrorCodeNotBetween(Integer value1, Integer value2) {
            addCriterion("errorCode not between", value1, value2, "errorCode");
            return (Criteria) this;
        }

        public Criteria andErrorMsgIsNull() {
            addCriterion("errorMsg is null");
            return (Criteria) this;
        }

        public Criteria andErrorMsgIsNotNull() {
            addCriterion("errorMsg is not null");
            return (Criteria) this;
        }

        public Criteria andErrorMsgEqualTo(String value) {
            addCriterion("errorMsg =", value, "errorMsg");
            return (Criteria) this;
        }

        public Criteria andErrorMsgNotEqualTo(String value) {
            addCriterion("errorMsg <>", value, "errorMsg");
            return (Criteria) this;
        }

        public Criteria andErrorMsgGreaterThan(String value) {
            addCriterion("errorMsg >", value, "errorMsg");
            return (Criteria) this;
        }

        public Criteria andErrorMsgGreaterThanOrEqualTo(String value) {
            addCriterion("errorMsg >=", value, "errorMsg");
            return (Criteria) this;
        }

        public Criteria andErrorMsgLessThan(String value) {
            addCriterion("errorMsg <", value, "errorMsg");
            return (Criteria) this;
        }

        public Criteria andErrorMsgLessThanOrEqualTo(String value) {
            addCriterion("errorMsg <=", value, "errorMsg");
            return (Criteria) this;
        }

        public Criteria andErrorMsgLike(String value) {
            addCriterion("errorMsg like", value, "errorMsg");
            return (Criteria) this;
        }

        public Criteria andErrorMsgNotLike(String value) {
            addCriterion("errorMsg not like", value, "errorMsg");
            return (Criteria) this;
        }

        public Criteria andErrorMsgIn(List<String> values) {
            addCriterion("errorMsg in", values, "errorMsg");
            return (Criteria) this;
        }

        public Criteria andErrorMsgNotIn(List<String> values) {
            addCriterion("errorMsg not in", values, "errorMsg");
            return (Criteria) this;
        }

        public Criteria andErrorMsgBetween(String value1, String value2) {
            addCriterion("errorMsg between", value1, value2, "errorMsg");
            return (Criteria) this;
        }

        public Criteria andErrorMsgNotBetween(String value1, String value2) {
            addCriterion("errorMsg not between", value1, value2, "errorMsg");
            return (Criteria) this;
        }

        public Criteria andLookupInfoIsNull() {
            addCriterion("lookupInfo is null");
            return (Criteria) this;
        }

        public Criteria andLookupInfoIsNotNull() {
            addCriterion("lookupInfo is not null");
            return (Criteria) this;
        }

        public Criteria andLookupInfoEqualTo(String value) {
            addCriterion("lookupInfo =", value, "lookupInfo");
            return (Criteria) this;
        }

        public Criteria andLookupInfoNotEqualTo(String value) {
            addCriterion("lookupInfo <>", value, "lookupInfo");
            return (Criteria) this;
        }

        public Criteria andLookupInfoGreaterThan(String value) {
            addCriterion("lookupInfo >", value, "lookupInfo");
            return (Criteria) this;
        }

        public Criteria andLookupInfoGreaterThanOrEqualTo(String value) {
            addCriterion("lookupInfo >=", value, "lookupInfo");
            return (Criteria) this;
        }

        public Criteria andLookupInfoLessThan(String value) {
            addCriterion("lookupInfo <", value, "lookupInfo");
            return (Criteria) this;
        }

        public Criteria andLookupInfoLessThanOrEqualTo(String value) {
            addCriterion("lookupInfo <=", value, "lookupInfo");
            return (Criteria) this;
        }

        public Criteria andLookupInfoLike(String value) {
            addCriterion("lookupInfo like", value, "lookupInfo");
            return (Criteria) this;
        }

        public Criteria andLookupInfoNotLike(String value) {
            addCriterion("lookupInfo not like", value, "lookupInfo");
            return (Criteria) this;
        }

        public Criteria andLookupInfoIn(List<String> values) {
            addCriterion("lookupInfo in", values, "lookupInfo");
            return (Criteria) this;
        }

        public Criteria andLookupInfoNotIn(List<String> values) {
            addCriterion("lookupInfo not in", values, "lookupInfo");
            return (Criteria) this;
        }

        public Criteria andLookupInfoBetween(String value1, String value2) {
            addCriterion("lookupInfo between", value1, value2, "lookupInfo");
            return (Criteria) this;
        }

        public Criteria andLookupInfoNotBetween(String value1, String value2) {
            addCriterion("lookupInfo not between", value1, value2, "lookupInfo");
            return (Criteria) this;
        }

        public Criteria andErrorDescriptionIsNull() {
            addCriterion("errorDescription is null");
            return (Criteria) this;
        }

        public Criteria andErrorDescriptionIsNotNull() {
            addCriterion("errorDescription is not null");
            return (Criteria) this;
        }

        public Criteria andErrorDescriptionEqualTo(String value) {
            addCriterion("errorDescription =", value, "errorDescription");
            return (Criteria) this;
        }

        public Criteria andErrorDescriptionNotEqualTo(String value) {
            addCriterion("errorDescription <>", value, "errorDescription");
            return (Criteria) this;
        }

        public Criteria andErrorDescriptionGreaterThan(String value) {
            addCriterion("errorDescription >", value, "errorDescription");
            return (Criteria) this;
        }

        public Criteria andErrorDescriptionGreaterThanOrEqualTo(String value) {
            addCriterion("errorDescription >=", value, "errorDescription");
            return (Criteria) this;
        }

        public Criteria andErrorDescriptionLessThan(String value) {
            addCriterion("errorDescription <", value, "errorDescription");
            return (Criteria) this;
        }

        public Criteria andErrorDescriptionLessThanOrEqualTo(String value) {
            addCriterion("errorDescription <=", value, "errorDescription");
            return (Criteria) this;
        }

        public Criteria andErrorDescriptionLike(String value) {
            addCriterion("errorDescription like", value, "errorDescription");
            return (Criteria) this;
        }

        public Criteria andErrorDescriptionNotLike(String value) {
            addCriterion("errorDescription not like", value, "errorDescription");
            return (Criteria) this;
        }

        public Criteria andErrorDescriptionIn(List<String> values) {
            addCriterion("errorDescription in", values, "errorDescription");
            return (Criteria) this;
        }

        public Criteria andErrorDescriptionNotIn(List<String> values) {
            addCriterion("errorDescription not in", values, "errorDescription");
            return (Criteria) this;
        }

        public Criteria andErrorDescriptionBetween(String value1, String value2) {
            addCriterion("errorDescription between", value1, value2, "errorDescription");
            return (Criteria) this;
        }

        public Criteria andErrorDescriptionNotBetween(String value1, String value2) {
            addCriterion("errorDescription not between", value1, value2, "errorDescription");
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
