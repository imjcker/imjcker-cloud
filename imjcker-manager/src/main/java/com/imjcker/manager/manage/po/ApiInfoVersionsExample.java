package com.imjcker.manager.manage.po;

import java.util.ArrayList;
import java.util.List;

public class ApiInfoVersionsExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ApiInfoVersionsExample() {
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

        public Criteria andApiNameIsNull() {
            addCriterion("apiName is null");
            return (Criteria) this;
        }

        public Criteria andApiNameIsNotNull() {
            addCriterion("apiName is not null");
            return (Criteria) this;
        }

        public Criteria andApiNameEqualTo(String value) {
            addCriterion("apiName =", value, "apiName");
            return (Criteria) this;
        }

        public Criteria andApiNameNotEqualTo(String value) {
            addCriterion("apiName <>", value, "apiName");
            return (Criteria) this;
        }

        public Criteria andApiNameGreaterThan(String value) {
            addCriterion("apiName >", value, "apiName");
            return (Criteria) this;
        }

        public Criteria andApiNameGreaterThanOrEqualTo(String value) {
            addCriterion("apiName >=", value, "apiName");
            return (Criteria) this;
        }

        public Criteria andApiNameLessThan(String value) {
            addCriterion("apiName <", value, "apiName");
            return (Criteria) this;
        }

        public Criteria andApiNameLessThanOrEqualTo(String value) {
            addCriterion("apiName <=", value, "apiName");
            return (Criteria) this;
        }

        public Criteria andApiNameLike(String value) {
            addCriterion("apiName like", value, "apiName");
            return (Criteria) this;
        }

        public Criteria andApiNameNotLike(String value) {
            addCriterion("apiName not like", value, "apiName");
            return (Criteria) this;
        }

        public Criteria andApiNameIn(List<String> values) {
            addCriterion("apiName in", values, "apiName");
            return (Criteria) this;
        }

        public Criteria andApiNameNotIn(List<String> values) {
            addCriterion("apiName not in", values, "apiName");
            return (Criteria) this;
        }

        public Criteria andApiNameBetween(String value1, String value2) {
            addCriterion("apiName between", value1, value2, "apiName");
            return (Criteria) this;
        }

        public Criteria andApiNameNotBetween(String value1, String value2) {
            addCriterion("apiName not between", value1, value2, "apiName");
            return (Criteria) this;
        }

        public Criteria andApiGroupIdIsNull() {
            addCriterion("apiGroupId is null");
            return (Criteria) this;
        }

        public Criteria andApiGroupIdIsNotNull() {
            addCriterion("apiGroupId is not null");
            return (Criteria) this;
        }

        public Criteria andApiGroupIdEqualTo(Integer value) {
            addCriterion("apiGroupId =", value, "apiGroupId");
            return (Criteria) this;
        }

        public Criteria andApiGroupIdNotEqualTo(Integer value) {
            addCriterion("apiGroupId <>", value, "apiGroupId");
            return (Criteria) this;
        }

        public Criteria andApiGroupIdGreaterThan(Integer value) {
            addCriterion("apiGroupId >", value, "apiGroupId");
            return (Criteria) this;
        }

        public Criteria andApiGroupIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("apiGroupId >=", value, "apiGroupId");
            return (Criteria) this;
        }

        public Criteria andApiGroupIdLessThan(Integer value) {
            addCriterion("apiGroupId <", value, "apiGroupId");
            return (Criteria) this;
        }

        public Criteria andApiGroupIdLessThanOrEqualTo(Integer value) {
            addCriterion("apiGroupId <=", value, "apiGroupId");
            return (Criteria) this;
        }

        public Criteria andApiGroupIdIn(List<Integer> values) {
            addCriterion("apiGroupId in", values, "apiGroupId");
            return (Criteria) this;
        }

        public Criteria andApiGroupIdNotIn(List<Integer> values) {
            addCriterion("apiGroupId not in", values, "apiGroupId");
            return (Criteria) this;
        }

        public Criteria andApiGroupIdBetween(Integer value1, Integer value2) {
            addCriterion("apiGroupId between", value1, value2, "apiGroupId");
            return (Criteria) this;
        }

        public Criteria andApiGroupIdNotBetween(Integer value1, Integer value2) {
            addCriterion("apiGroupId not between", value1, value2, "apiGroupId");
            return (Criteria) this;
        }

        public Criteria andHttpPathIsNull() {
            addCriterion("httpPath is null");
            return (Criteria) this;
        }

        public Criteria andHttpPathIsNotNull() {
            addCriterion("httpPath is not null");
            return (Criteria) this;
        }

        public Criteria andHttpPathEqualTo(String value) {
            addCriterion("httpPath =", value, "httpPath");
            return (Criteria) this;
        }

        public Criteria andHttpPathNotEqualTo(String value) {
            addCriterion("httpPath <>", value, "httpPath");
            return (Criteria) this;
        }

        public Criteria andHttpPathGreaterThan(String value) {
            addCriterion("httpPath >", value, "httpPath");
            return (Criteria) this;
        }

        public Criteria andHttpPathGreaterThanOrEqualTo(String value) {
            addCriterion("httpPath >=", value, "httpPath");
            return (Criteria) this;
        }

        public Criteria andHttpPathLessThan(String value) {
            addCriterion("httpPath <", value, "httpPath");
            return (Criteria) this;
        }

        public Criteria andHttpPathLessThanOrEqualTo(String value) {
            addCriterion("httpPath <=", value, "httpPath");
            return (Criteria) this;
        }

        public Criteria andHttpPathLike(String value) {
            addCriterion("httpPath like", value, "httpPath");
            return (Criteria) this;
        }

        public Criteria andHttpPathNotLike(String value) {
            addCriterion("httpPath not like", value, "httpPath");
            return (Criteria) this;
        }

        public Criteria andHttpPathIn(List<String> values) {
            addCriterion("httpPath in", values, "httpPath");
            return (Criteria) this;
        }

        public Criteria andHttpPathNotIn(List<String> values) {
            addCriterion("httpPath not in", values, "httpPath");
            return (Criteria) this;
        }

        public Criteria andHttpPathBetween(String value1, String value2) {
            addCriterion("httpPath between", value1, value2, "httpPath");
            return (Criteria) this;
        }

        public Criteria andHttpPathNotBetween(String value1, String value2) {
            addCriterion("httpPath not between", value1, value2, "httpPath");
            return (Criteria) this;
        }

        public Criteria andHttpMethodIsNull() {
            addCriterion("httpMethod is null");
            return (Criteria) this;
        }

        public Criteria andHttpMethodIsNotNull() {
            addCriterion("httpMethod is not null");
            return (Criteria) this;
        }

        public Criteria andHttpMethodEqualTo(Integer value) {
            addCriterion("httpMethod =", value, "httpMethod");
            return (Criteria) this;
        }

        public Criteria andHttpMethodNotEqualTo(Integer value) {
            addCriterion("httpMethod <>", value, "httpMethod");
            return (Criteria) this;
        }

        public Criteria andHttpMethodGreaterThan(Integer value) {
            addCriterion("httpMethod >", value, "httpMethod");
            return (Criteria) this;
        }

        public Criteria andHttpMethodGreaterThanOrEqualTo(Integer value) {
            addCriterion("httpMethod >=", value, "httpMethod");
            return (Criteria) this;
        }

        public Criteria andHttpMethodLessThan(Integer value) {
            addCriterion("httpMethod <", value, "httpMethod");
            return (Criteria) this;
        }

        public Criteria andHttpMethodLessThanOrEqualTo(Integer value) {
            addCriterion("httpMethod <=", value, "httpMethod");
            return (Criteria) this;
        }

        public Criteria andHttpMethodIn(List<Integer> values) {
            addCriterion("httpMethod in", values, "httpMethod");
            return (Criteria) this;
        }

        public Criteria andHttpMethodNotIn(List<Integer> values) {
            addCriterion("httpMethod not in", values, "httpMethod");
            return (Criteria) this;
        }

        public Criteria andHttpMethodBetween(Integer value1, Integer value2) {
            addCriterion("httpMethod between", value1, value2, "httpMethod");
            return (Criteria) this;
        }

        public Criteria andHttpMethodNotBetween(Integer value1, Integer value2) {
            addCriterion("httpMethod not between", value1, value2, "httpMethod");
            return (Criteria) this;
        }

        public Criteria andBackEndAddressIsNull() {
            addCriterion("backEndAddress is null");
            return (Criteria) this;
        }

        public Criteria andBackEndAddressIsNotNull() {
            addCriterion("backEndAddress is not null");
            return (Criteria) this;
        }

        public Criteria andBackEndAddressEqualTo(String value) {
            addCriterion("backEndAddress =", value, "backEndAddress");
            return (Criteria) this;
        }

        public Criteria andBackEndAddressNotEqualTo(String value) {
            addCriterion("backEndAddress <>", value, "backEndAddress");
            return (Criteria) this;
        }

        public Criteria andBackEndAddressGreaterThan(String value) {
            addCriterion("backEndAddress >", value, "backEndAddress");
            return (Criteria) this;
        }

        public Criteria andBackEndAddressGreaterThanOrEqualTo(String value) {
            addCriterion("backEndAddress >=", value, "backEndAddress");
            return (Criteria) this;
        }

        public Criteria andBackEndAddressLessThan(String value) {
            addCriterion("backEndAddress <", value, "backEndAddress");
            return (Criteria) this;
        }

        public Criteria andBackEndAddressLessThanOrEqualTo(String value) {
            addCriterion("backEndAddress <=", value, "backEndAddress");
            return (Criteria) this;
        }

        public Criteria andBackEndAddressLike(String value) {
            addCriterion("backEndAddress like", value, "backEndAddress");
            return (Criteria) this;
        }

        public Criteria andBackEndAddressNotLike(String value) {
            addCriterion("backEndAddress not like", value, "backEndAddress");
            return (Criteria) this;
        }

        public Criteria andBackEndAddressIn(List<String> values) {
            addCriterion("backEndAddress in", values, "backEndAddress");
            return (Criteria) this;
        }

        public Criteria andBackEndAddressNotIn(List<String> values) {
            addCriterion("backEndAddress not in", values, "backEndAddress");
            return (Criteria) this;
        }

        public Criteria andBackEndAddressBetween(String value1, String value2) {
            addCriterion("backEndAddress between", value1, value2, "backEndAddress");
            return (Criteria) this;
        }

        public Criteria andBackEndAddressNotBetween(String value1, String value2) {
            addCriterion("backEndAddress not between", value1, value2, "backEndAddress");
            return (Criteria) this;
        }

        public Criteria andBackEndPathIsNull() {
            addCriterion("backEndPath is null");
            return (Criteria) this;
        }

        public Criteria andBackEndPathIsNotNull() {
            addCriterion("backEndPath is not null");
            return (Criteria) this;
        }

        public Criteria andBackEndPathEqualTo(String value) {
            addCriterion("backEndPath =", value, "backEndPath");
            return (Criteria) this;
        }

        public Criteria andBackEndPathNotEqualTo(String value) {
            addCriterion("backEndPath <>", value, "backEndPath");
            return (Criteria) this;
        }

        public Criteria andBackEndPathGreaterThan(String value) {
            addCriterion("backEndPath >", value, "backEndPath");
            return (Criteria) this;
        }

        public Criteria andBackEndPathGreaterThanOrEqualTo(String value) {
            addCriterion("backEndPath >=", value, "backEndPath");
            return (Criteria) this;
        }

        public Criteria andBackEndPathLessThan(String value) {
            addCriterion("backEndPath <", value, "backEndPath");
            return (Criteria) this;
        }

        public Criteria andBackEndPathLessThanOrEqualTo(String value) {
            addCriterion("backEndPath <=", value, "backEndPath");
            return (Criteria) this;
        }

        public Criteria andBackEndPathLike(String value) {
            addCriterion("backEndPath like", value, "backEndPath");
            return (Criteria) this;
        }

        public Criteria andBackEndPathNotLike(String value) {
            addCriterion("backEndPath not like", value, "backEndPath");
            return (Criteria) this;
        }

        public Criteria andBackEndPathIn(List<String> values) {
            addCriterion("backEndPath in", values, "backEndPath");
            return (Criteria) this;
        }

        public Criteria andBackEndPathNotIn(List<String> values) {
            addCriterion("backEndPath not in", values, "backEndPath");
            return (Criteria) this;
        }

        public Criteria andBackEndPathBetween(String value1, String value2) {
            addCriterion("backEndPath between", value1, value2, "backEndPath");
            return (Criteria) this;
        }

        public Criteria andBackEndPathNotBetween(String value1, String value2) {
            addCriterion("backEndPath not between", value1, value2, "backEndPath");
            return (Criteria) this;
        }

        public Criteria andBackEndHttpMethodIsNull() {
            addCriterion("backEndHttpMethod is null");
            return (Criteria) this;
        }

        public Criteria andBackEndHttpMethodIsNotNull() {
            addCriterion("backEndHttpMethod is not null");
            return (Criteria) this;
        }

        public Criteria andBackEndHttpMethodEqualTo(Integer value) {
            addCriterion("backEndHttpMethod =", value, "backEndHttpMethod");
            return (Criteria) this;
        }

        public Criteria andBackEndHttpMethodNotEqualTo(Integer value) {
            addCriterion("backEndHttpMethod <>", value, "backEndHttpMethod");
            return (Criteria) this;
        }

        public Criteria andBackEndHttpMethodGreaterThan(Integer value) {
            addCriterion("backEndHttpMethod >", value, "backEndHttpMethod");
            return (Criteria) this;
        }

        public Criteria andBackEndHttpMethodGreaterThanOrEqualTo(Integer value) {
            addCriterion("backEndHttpMethod >=", value, "backEndHttpMethod");
            return (Criteria) this;
        }

        public Criteria andBackEndHttpMethodLessThan(Integer value) {
            addCriterion("backEndHttpMethod <", value, "backEndHttpMethod");
            return (Criteria) this;
        }

        public Criteria andBackEndHttpMethodLessThanOrEqualTo(Integer value) {
            addCriterion("backEndHttpMethod <=", value, "backEndHttpMethod");
            return (Criteria) this;
        }

        public Criteria andBackEndHttpMethodIn(List<Integer> values) {
            addCriterion("backEndHttpMethod in", values, "backEndHttpMethod");
            return (Criteria) this;
        }

        public Criteria andBackEndHttpMethodNotIn(List<Integer> values) {
            addCriterion("backEndHttpMethod not in", values, "backEndHttpMethod");
            return (Criteria) this;
        }

        public Criteria andBackEndHttpMethodBetween(Integer value1, Integer value2) {
            addCriterion("backEndHttpMethod between", value1, value2, "backEndHttpMethod");
            return (Criteria) this;
        }

        public Criteria andBackEndHttpMethodNotBetween(Integer value1, Integer value2) {
            addCriterion("backEndHttpMethod not between", value1, value2, "backEndHttpMethod");
            return (Criteria) this;
        }

        public Criteria andBackEndTimeoutIsNull() {
            addCriterion("backEndTimeout is null");
            return (Criteria) this;
        }

        public Criteria andBackEndTimeoutIsNotNull() {
            addCriterion("backEndTimeout is not null");
            return (Criteria) this;
        }

        public Criteria andBackEndTimeoutEqualTo(Integer value) {
            addCriterion("backEndTimeout =", value, "backEndTimeout");
            return (Criteria) this;
        }

        public Criteria andBackEndTimeoutNotEqualTo(Integer value) {
            addCriterion("backEndTimeout <>", value, "backEndTimeout");
            return (Criteria) this;
        }

        public Criteria andBackEndTimeoutGreaterThan(Integer value) {
            addCriterion("backEndTimeout >", value, "backEndTimeout");
            return (Criteria) this;
        }

        public Criteria andBackEndTimeoutGreaterThanOrEqualTo(Integer value) {
            addCriterion("backEndTimeout >=", value, "backEndTimeout");
            return (Criteria) this;
        }

        public Criteria andBackEndTimeoutLessThan(Integer value) {
            addCriterion("backEndTimeout <", value, "backEndTimeout");
            return (Criteria) this;
        }

        public Criteria andBackEndTimeoutLessThanOrEqualTo(Integer value) {
            addCriterion("backEndTimeout <=", value, "backEndTimeout");
            return (Criteria) this;
        }

        public Criteria andBackEndTimeoutIn(List<Integer> values) {
            addCriterion("backEndTimeout in", values, "backEndTimeout");
            return (Criteria) this;
        }

        public Criteria andBackEndTimeoutNotIn(List<Integer> values) {
            addCriterion("backEndTimeout not in", values, "backEndTimeout");
            return (Criteria) this;
        }

        public Criteria andBackEndTimeoutBetween(Integer value1, Integer value2) {
            addCriterion("backEndTimeout between", value1, value2, "backEndTimeout");
            return (Criteria) this;
        }

        public Criteria andBackEndTimeoutNotBetween(Integer value1, Integer value2) {
            addCriterion("backEndTimeout not between", value1, value2, "backEndTimeout");
            return (Criteria) this;
        }

        public Criteria andIsMockIsNull() {
            addCriterion("isMock is null");
            return (Criteria) this;
        }

        public Criteria andIsMockIsNotNull() {
            addCriterion("isMock is not null");
            return (Criteria) this;
        }

        public Criteria andIsMockEqualTo(Integer value) {
            addCriterion("isMock =", value, "isMock");
            return (Criteria) this;
        }

        public Criteria andIsMockNotEqualTo(Integer value) {
            addCriterion("isMock <>", value, "isMock");
            return (Criteria) this;
        }

        public Criteria andIsMockGreaterThan(Integer value) {
            addCriterion("isMock >", value, "isMock");
            return (Criteria) this;
        }

        public Criteria andIsMockGreaterThanOrEqualTo(Integer value) {
            addCriterion("isMock >=", value, "isMock");
            return (Criteria) this;
        }

        public Criteria andIsMockLessThan(Integer value) {
            addCriterion("isMock <", value, "isMock");
            return (Criteria) this;
        }

        public Criteria andIsMockLessThanOrEqualTo(Integer value) {
            addCriterion("isMock <=", value, "isMock");
            return (Criteria) this;
        }

        public Criteria andIsMockIn(List<Integer> values) {
            addCriterion("isMock in", values, "isMock");
            return (Criteria) this;
        }

        public Criteria andIsMockNotIn(List<Integer> values) {
            addCriterion("isMock not in", values, "isMock");
            return (Criteria) this;
        }

        public Criteria andIsMockBetween(Integer value1, Integer value2) {
            addCriterion("isMock between", value1, value2, "isMock");
            return (Criteria) this;
        }

        public Criteria andIsMockNotBetween(Integer value1, Integer value2) {
            addCriterion("isMock not between", value1, value2, "isMock");
            return (Criteria) this;
        }

        public Criteria andCallBackTypeIsNull() {
            addCriterion("callBackType is null");
            return (Criteria) this;
        }

        public Criteria andCallBackTypeIsNotNull() {
            addCriterion("callBackType is not null");
            return (Criteria) this;
        }

        public Criteria andCallBackTypeEqualTo(Integer value) {
            addCriterion("callBackType =", value, "callBackType");
            return (Criteria) this;
        }

        public Criteria andCallBackTypeNotEqualTo(Integer value) {
            addCriterion("callBackType <>", value, "callBackType");
            return (Criteria) this;
        }

        public Criteria andCallBackTypeGreaterThan(Integer value) {
            addCriterion("callBackType >", value, "callBackType");
            return (Criteria) this;
        }

        public Criteria andCallBackTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("callBackType >=", value, "callBackType");
            return (Criteria) this;
        }

        public Criteria andCallBackTypeLessThan(Integer value) {
            addCriterion("callBackType <", value, "callBackType");
            return (Criteria) this;
        }

        public Criteria andCallBackTypeLessThanOrEqualTo(Integer value) {
            addCriterion("callBackType <=", value, "callBackType");
            return (Criteria) this;
        }

        public Criteria andCallBackTypeIn(List<Integer> values) {
            addCriterion("callBackType in", values, "callBackType");
            return (Criteria) this;
        }

        public Criteria andCallBackTypeNotIn(List<Integer> values) {
            addCriterion("callBackType not in", values, "callBackType");
            return (Criteria) this;
        }

        public Criteria andCallBackTypeBetween(Integer value1, Integer value2) {
            addCriterion("callBackType between", value1, value2, "callBackType");
            return (Criteria) this;
        }

        public Criteria andCallBackTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("callBackType not between", value1, value2, "callBackType");
            return (Criteria) this;
        }

        public Criteria andEnvIsNull() {
            addCriterion("env is null");
            return (Criteria) this;
        }

        public Criteria andEnvIsNotNull() {
            addCriterion("env is not null");
            return (Criteria) this;
        }

        public Criteria andEnvEqualTo(Integer value) {
            addCriterion("env =", value, "env");
            return (Criteria) this;
        }

        public Criteria andEnvNotEqualTo(Integer value) {
            addCriterion("env <>", value, "env");
            return (Criteria) this;
        }

        public Criteria andEnvGreaterThan(Integer value) {
            addCriterion("env >", value, "env");
            return (Criteria) this;
        }

        public Criteria andEnvGreaterThanOrEqualTo(Integer value) {
            addCriterion("env >=", value, "env");
            return (Criteria) this;
        }

        public Criteria andEnvLessThan(Integer value) {
            addCriterion("env <", value, "env");
            return (Criteria) this;
        }

        public Criteria andEnvLessThanOrEqualTo(Integer value) {
            addCriterion("env <=", value, "env");
            return (Criteria) this;
        }

        public Criteria andEnvIn(List<Integer> values) {
            addCriterion("env in", values, "env");
            return (Criteria) this;
        }

        public Criteria andEnvNotIn(List<Integer> values) {
            addCriterion("env not in", values, "env");
            return (Criteria) this;
        }

        public Criteria andEnvBetween(Integer value1, Integer value2) {
            addCriterion("env between", value1, value2, "env");
            return (Criteria) this;
        }

        public Criteria andEnvNotBetween(Integer value1, Integer value2) {
            addCriterion("env not between", value1, value2, "env");
            return (Criteria) this;
        }

        public Criteria andCurrentVersionIsNull() {
            addCriterion("currentVersion is null");
            return (Criteria) this;
        }

        public Criteria andCurrentVersionIsNotNull() {
            addCriterion("currentVersion is not null");
            return (Criteria) this;
        }

        public Criteria andCurrentVersionEqualTo(Integer value) {
            addCriterion("currentVersion =", value, "currentVersion");
            return (Criteria) this;
        }

        public Criteria andCurrentVersionNotEqualTo(Integer value) {
            addCriterion("currentVersion <>", value, "currentVersion");
            return (Criteria) this;
        }

        public Criteria andCurrentVersionGreaterThan(Integer value) {
            addCriterion("currentVersion >", value, "currentVersion");
            return (Criteria) this;
        }

        public Criteria andCurrentVersionGreaterThanOrEqualTo(Integer value) {
            addCriterion("currentVersion >=", value, "currentVersion");
            return (Criteria) this;
        }

        public Criteria andCurrentVersionLessThan(Integer value) {
            addCriterion("currentVersion <", value, "currentVersion");
            return (Criteria) this;
        }

        public Criteria andCurrentVersionLessThanOrEqualTo(Integer value) {
            addCriterion("currentVersion <=", value, "currentVersion");
            return (Criteria) this;
        }

        public Criteria andCurrentVersionIn(List<Integer> values) {
            addCriterion("currentVersion in", values, "currentVersion");
            return (Criteria) this;
        }

        public Criteria andCurrentVersionNotIn(List<Integer> values) {
            addCriterion("currentVersion not in", values, "currentVersion");
            return (Criteria) this;
        }

        public Criteria andCurrentVersionBetween(Integer value1, Integer value2) {
            addCriterion("currentVersion between", value1, value2, "currentVersion");
            return (Criteria) this;
        }

        public Criteria andCurrentVersionNotBetween(Integer value1, Integer value2) {
            addCriterion("currentVersion not between", value1, value2, "currentVersion");
            return (Criteria) this;
        }

        public Criteria andPubDescriptionIsNull() {
            addCriterion("pubDescription is null");
            return (Criteria) this;
        }

        public Criteria andPubDescriptionIsNotNull() {
            addCriterion("pubDescription is not null");
            return (Criteria) this;
        }

        public Criteria andPubDescriptionEqualTo(String value) {
            addCriterion("pubDescription =", value, "pubDescription");
            return (Criteria) this;
        }

        public Criteria andPubDescriptionNotEqualTo(String value) {
            addCriterion("pubDescription <>", value, "pubDescription");
            return (Criteria) this;
        }

        public Criteria andPubDescriptionGreaterThan(String value) {
            addCriterion("pubDescription >", value, "pubDescription");
            return (Criteria) this;
        }

        public Criteria andPubDescriptionGreaterThanOrEqualTo(String value) {
            addCriterion("pubDescription >=", value, "pubDescription");
            return (Criteria) this;
        }

        public Criteria andPubDescriptionLessThan(String value) {
            addCriterion("pubDescription <", value, "pubDescription");
            return (Criteria) this;
        }

        public Criteria andPubDescriptionLessThanOrEqualTo(String value) {
            addCriterion("pubDescription <=", value, "pubDescription");
            return (Criteria) this;
        }

        public Criteria andPubDescriptionLike(String value) {
            addCriterion("pubDescription like", value, "pubDescription");
            return (Criteria) this;
        }

        public Criteria andPubDescriptionNotLike(String value) {
            addCriterion("pubDescription not like", value, "pubDescription");
            return (Criteria) this;
        }

        public Criteria andPubDescriptionIn(List<String> values) {
            addCriterion("pubDescription in", values, "pubDescription");
            return (Criteria) this;
        }

        public Criteria andPubDescriptionNotIn(List<String> values) {
            addCriterion("pubDescription not in", values, "pubDescription");
            return (Criteria) this;
        }

        public Criteria andPubDescriptionBetween(String value1, String value2) {
            addCriterion("pubDescription between", value1, value2, "pubDescription");
            return (Criteria) this;
        }

        public Criteria andPubDescriptionNotBetween(String value1, String value2) {
            addCriterion("pubDescription not between", value1, value2, "pubDescription");
            return (Criteria) this;
        }

        public Criteria andSaveMongoDBIsNull() {
            addCriterion("saveMongoDB is null");
            return (Criteria) this;
        }

        public Criteria andSaveMongoDBIsNotNull() {
            addCriterion("saveMongoDB is not null");
            return (Criteria) this;
        }

        public Criteria andSaveMongoDBEqualTo(Integer value) {
            addCriterion("saveMongoDB =", value, "saveMongoDB");
            return (Criteria) this;
        }

        public Criteria andSaveMongoDBNotEqualTo(Integer value) {
            addCriterion("saveMongoDB <>", value, "saveMongoDB");
            return (Criteria) this;
        }

        public Criteria andSaveMongoDBGreaterThan(Integer value) {
            addCriterion("saveMongoDB >", value, "saveMongoDB");
            return (Criteria) this;
        }

        public Criteria andSaveMongoDBGreaterThanOrEqualTo(Integer value) {
            addCriterion("saveMongoDB >=", value, "saveMongoDB");
            return (Criteria) this;
        }

        public Criteria andSaveMongoDBLessThan(Integer value) {
            addCriterion("saveMongoDB <", value, "saveMongoDB");
            return (Criteria) this;
        }

        public Criteria andSaveMongoDBLessThanOrEqualTo(Integer value) {
            addCriterion("saveMongoDB <=", value, "saveMongoDB");
            return (Criteria) this;
        }

        public Criteria andSaveMongoDBIn(List<Integer> values) {
            addCriterion("saveMongoDB in", values, "saveMongoDB");
            return (Criteria) this;
        }

        public Criteria andSaveMongoDBNotIn(List<Integer> values) {
            addCriterion("saveMongoDB not in", values, "saveMongoDB");
            return (Criteria) this;
        }

        public Criteria andSaveMongoDBBetween(Integer value1, Integer value2) {
            addCriterion("saveMongoDB between", value1, value2, "saveMongoDB");
            return (Criteria) this;
        }

        public Criteria andSaveMongoDBNotBetween(Integer value1, Integer value2) {
            addCriterion("saveMongoDB not between", value1, value2, "saveMongoDB");
            return (Criteria) this;
        }

        public Criteria andMongodbURIIsNull() {
            addCriterion("mongodbURI is null");
            return (Criteria) this;
        }

        public Criteria andMongodbURIIsNotNull() {
            addCriterion("mongodbURI is not null");
            return (Criteria) this;
        }

        public Criteria andMongodbURIEqualTo(String value) {
            addCriterion("mongodbURI =", value, "mongodbURI");
            return (Criteria) this;
        }

        public Criteria andMongodbURINotEqualTo(String value) {
            addCriterion("mongodbURI <>", value, "mongodbURI");
            return (Criteria) this;
        }

        public Criteria andMongodbURIGreaterThan(String value) {
            addCriterion("mongodbURI >", value, "mongodbURI");
            return (Criteria) this;
        }

        public Criteria andMongodbURIGreaterThanOrEqualTo(String value) {
            addCriterion("mongodbURI >=", value, "mongodbURI");
            return (Criteria) this;
        }

        public Criteria andMongodbURILessThan(String value) {
            addCriterion("mongodbURI <", value, "mongodbURI");
            return (Criteria) this;
        }

        public Criteria andMongodbURILessThanOrEqualTo(String value) {
            addCriterion("mongodbURI <=", value, "mongodbURI");
            return (Criteria) this;
        }

        public Criteria andMongodbURILike(String value) {
            addCriterion("mongodbURI like", value, "mongodbURI");
            return (Criteria) this;
        }

        public Criteria andMongodbURINotLike(String value) {
            addCriterion("mongodbURI not like", value, "mongodbURI");
            return (Criteria) this;
        }

        public Criteria andMongodbURIIn(List<String> values) {
            addCriterion("mongodbURI in", values, "mongodbURI");
            return (Criteria) this;
        }

        public Criteria andMongodbURINotIn(List<String> values) {
            addCriterion("mongodbURI not in", values, "mongodbURI");
            return (Criteria) this;
        }

        public Criteria andMongodbURIBetween(String value1, String value2) {
            addCriterion("mongodbURI between", value1, value2, "mongodbURI");
            return (Criteria) this;
        }

        public Criteria andMongodbURINotBetween(String value1, String value2) {
            addCriterion("mongodbURI not between", value1, value2, "mongodbURI");
            return (Criteria) this;
        }

        public Criteria andMongodbDBNameIsNull() {
            addCriterion("mongodbDBName is null");
            return (Criteria) this;
        }

        public Criteria andMongodbDBNameIsNotNull() {
            addCriterion("mongodbDBName is not null");
            return (Criteria) this;
        }

        public Criteria andMongodbDBNameEqualTo(String value) {
            addCriterion("mongodbDBName =", value, "mongodbDBName");
            return (Criteria) this;
        }

        public Criteria andMongodbDBNameNotEqualTo(String value) {
            addCriterion("mongodbDBName <>", value, "mongodbDBName");
            return (Criteria) this;
        }

        public Criteria andMongodbDBNameGreaterThan(String value) {
            addCriterion("mongodbDBName >", value, "mongodbDBName");
            return (Criteria) this;
        }

        public Criteria andMongodbDBNameGreaterThanOrEqualTo(String value) {
            addCriterion("mongodbDBName >=", value, "mongodbDBName");
            return (Criteria) this;
        }

        public Criteria andMongodbDBNameLessThan(String value) {
            addCriterion("mongodbDBName <", value, "mongodbDBName");
            return (Criteria) this;
        }

        public Criteria andMongodbDBNameLessThanOrEqualTo(String value) {
            addCriterion("mongodbDBName <=", value, "mongodbDBName");
            return (Criteria) this;
        }

        public Criteria andMongodbDBNameLike(String value) {
            addCriterion("mongodbDBName like", value, "mongodbDBName");
            return (Criteria) this;
        }

        public Criteria andMongodbDBNameNotLike(String value) {
            addCriterion("mongodbDBName not like", value, "mongodbDBName");
            return (Criteria) this;
        }

        public Criteria andMongodbDBNameIn(List<String> values) {
            addCriterion("mongodbDBName in", values, "mongodbDBName");
            return (Criteria) this;
        }

        public Criteria andMongodbDBNameNotIn(List<String> values) {
            addCriterion("mongodbDBName not in", values, "mongodbDBName");
            return (Criteria) this;
        }

        public Criteria andMongodbDBNameBetween(String value1, String value2) {
            addCriterion("mongodbDBName between", value1, value2, "mongodbDBName");
            return (Criteria) this;
        }

        public Criteria andMongodbDBNameNotBetween(String value1, String value2) {
            addCriterion("mongodbDBName not between", value1, value2, "mongodbDBName");
            return (Criteria) this;
        }

        public Criteria andMongodbCollectionNameIsNull() {
            addCriterion("mongodbCollectionName is null");
            return (Criteria) this;
        }

        public Criteria andMongodbCollectionNameIsNotNull() {
            addCriterion("mongodbCollectionName is not null");
            return (Criteria) this;
        }

        public Criteria andMongodbCollectionNameEqualTo(String value) {
            addCriterion("mongodbCollectionName =", value, "mongodbCollectionName");
            return (Criteria) this;
        }

        public Criteria andMongodbCollectionNameNotEqualTo(String value) {
            addCriterion("mongodbCollectionName <>", value, "mongodbCollectionName");
            return (Criteria) this;
        }

        public Criteria andMongodbCollectionNameGreaterThan(String value) {
            addCriterion("mongodbCollectionName >", value, "mongodbCollectionName");
            return (Criteria) this;
        }

        public Criteria andMongodbCollectionNameGreaterThanOrEqualTo(String value) {
            addCriterion("mongodbCollectionName >=", value, "mongodbCollectionName");
            return (Criteria) this;
        }

        public Criteria andMongodbCollectionNameLessThan(String value) {
            addCriterion("mongodbCollectionName <", value, "mongodbCollectionName");
            return (Criteria) this;
        }

        public Criteria andMongodbCollectionNameLessThanOrEqualTo(String value) {
            addCriterion("mongodbCollectionName <=", value, "mongodbCollectionName");
            return (Criteria) this;
        }

        public Criteria andMongodbCollectionNameLike(String value) {
            addCriterion("mongodbCollectionName like", value, "mongodbCollectionName");
            return (Criteria) this;
        }

        public Criteria andMongodbCollectionNameNotLike(String value) {
            addCriterion("mongodbCollectionName not like", value, "mongodbCollectionName");
            return (Criteria) this;
        }

        public Criteria andMongodbCollectionNameIn(List<String> values) {
            addCriterion("mongodbCollectionName in", values, "mongodbCollectionName");
            return (Criteria) this;
        }

        public Criteria andMongodbCollectionNameNotIn(List<String> values) {
            addCriterion("mongodbCollectionName not in", values, "mongodbCollectionName");
            return (Criteria) this;
        }

        public Criteria andMongodbCollectionNameBetween(String value1, String value2) {
            addCriterion("mongodbCollectionName between", value1, value2, "mongodbCollectionName");
            return (Criteria) this;
        }

        public Criteria andMongodbCollectionNameNotBetween(String value1, String value2) {
            addCriterion("mongodbCollectionName not between", value1, value2, "mongodbCollectionName");
            return (Criteria) this;
        }

        public Criteria andSaveMQIsNull() {
            addCriterion("saveMQ is null");
            return (Criteria) this;
        }

        public Criteria andSaveMQIsNotNull() {
            addCriterion("saveMQ is not null");
            return (Criteria) this;
        }

        public Criteria andSaveMQEqualTo(Integer value) {
            addCriterion("saveMQ =", value, "saveMQ");
            return (Criteria) this;
        }

        public Criteria andSaveMQNotEqualTo(Integer value) {
            addCriterion("saveMQ <>", value, "saveMQ");
            return (Criteria) this;
        }

        public Criteria andSaveMQGreaterThan(Integer value) {
            addCriterion("saveMQ >", value, "saveMQ");
            return (Criteria) this;
        }

        public Criteria andSaveMQGreaterThanOrEqualTo(Integer value) {
            addCriterion("saveMQ >=", value, "saveMQ");
            return (Criteria) this;
        }

        public Criteria andSaveMQLessThan(Integer value) {
            addCriterion("saveMQ <", value, "saveMQ");
            return (Criteria) this;
        }

        public Criteria andSaveMQLessThanOrEqualTo(Integer value) {
            addCriterion("saveMQ <=", value, "saveMQ");
            return (Criteria) this;
        }

        public Criteria andSaveMQIn(List<Integer> values) {
            addCriterion("saveMQ in", values, "saveMQ");
            return (Criteria) this;
        }

        public Criteria andSaveMQNotIn(List<Integer> values) {
            addCriterion("saveMQ not in", values, "saveMQ");
            return (Criteria) this;
        }

        public Criteria andSaveMQBetween(Integer value1, Integer value2) {
            addCriterion("saveMQ between", value1, value2, "saveMQ");
            return (Criteria) this;
        }

        public Criteria andSaveMQNotBetween(Integer value1, Integer value2) {
            addCriterion("saveMQ not between", value1, value2, "saveMQ");
            return (Criteria) this;
        }

        public Criteria andMqTypeIsNull() {
            addCriterion("mqType is null");
            return (Criteria) this;
        }

        public Criteria andMqTypeIsNotNull() {
            addCriterion("mqType is not null");
            return (Criteria) this;
        }

        public Criteria andMqTypeEqualTo(Integer value) {
            addCriterion("mqType =", value, "mqType");
            return (Criteria) this;
        }

        public Criteria andMqTypeNotEqualTo(Integer value) {
            addCriterion("mqType <>", value, "mqType");
            return (Criteria) this;
        }

        public Criteria andMqTypeGreaterThan(Integer value) {
            addCriterion("mqType >", value, "mqType");
            return (Criteria) this;
        }

        public Criteria andMqTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("mqType >=", value, "mqType");
            return (Criteria) this;
        }

        public Criteria andMqTypeLessThan(Integer value) {
            addCriterion("mqType <", value, "mqType");
            return (Criteria) this;
        }

        public Criteria andMqTypeLessThanOrEqualTo(Integer value) {
            addCriterion("mqType <=", value, "mqType");
            return (Criteria) this;
        }

        public Criteria andMqTypeIn(List<Integer> values) {
            addCriterion("mqType in", values, "mqType");
            return (Criteria) this;
        }

        public Criteria andMqTypeNotIn(List<Integer> values) {
            addCriterion("mqType not in", values, "mqType");
            return (Criteria) this;
        }

        public Criteria andMqTypeBetween(Integer value1, Integer value2) {
            addCriterion("mqType between", value1, value2, "mqType");
            return (Criteria) this;
        }

        public Criteria andMqTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("mqType not between", value1, value2, "mqType");
            return (Criteria) this;
        }

        public Criteria andMqAddressIsNull() {
            addCriterion("mqAddress is null");
            return (Criteria) this;
        }

        public Criteria andMqAddressIsNotNull() {
            addCriterion("mqAddress is not null");
            return (Criteria) this;
        }

        public Criteria andMqAddressEqualTo(String value) {
            addCriterion("mqAddress =", value, "mqAddress");
            return (Criteria) this;
        }

        public Criteria andMqAddressNotEqualTo(String value) {
            addCriterion("mqAddress <>", value, "mqAddress");
            return (Criteria) this;
        }

        public Criteria andMqAddressGreaterThan(String value) {
            addCriterion("mqAddress >", value, "mqAddress");
            return (Criteria) this;
        }

        public Criteria andMqAddressGreaterThanOrEqualTo(String value) {
            addCriterion("mqAddress >=", value, "mqAddress");
            return (Criteria) this;
        }

        public Criteria andMqAddressLessThan(String value) {
            addCriterion("mqAddress <", value, "mqAddress");
            return (Criteria) this;
        }

        public Criteria andMqAddressLessThanOrEqualTo(String value) {
            addCriterion("mqAddress <=", value, "mqAddress");
            return (Criteria) this;
        }

        public Criteria andMqAddressLike(String value) {
            addCriterion("mqAddress like", value, "mqAddress");
            return (Criteria) this;
        }

        public Criteria andMqAddressNotLike(String value) {
            addCriterion("mqAddress not like", value, "mqAddress");
            return (Criteria) this;
        }

        public Criteria andMqAddressIn(List<String> values) {
            addCriterion("mqAddress in", values, "mqAddress");
            return (Criteria) this;
        }

        public Criteria andMqAddressNotIn(List<String> values) {
            addCriterion("mqAddress not in", values, "mqAddress");
            return (Criteria) this;
        }

        public Criteria andMqAddressBetween(String value1, String value2) {
            addCriterion("mqAddress between", value1, value2, "mqAddress");
            return (Criteria) this;
        }

        public Criteria andMqAddressNotBetween(String value1, String value2) {
            addCriterion("mqAddress not between", value1, value2, "mqAddress");
            return (Criteria) this;
        }

        public Criteria andMqUserNameIsNull() {
            addCriterion("mqUserName is null");
            return (Criteria) this;
        }

        public Criteria andMqUserNameIsNotNull() {
            addCriterion("mqUserName is not null");
            return (Criteria) this;
        }

        public Criteria andMqUserNameEqualTo(String value) {
            addCriterion("mqUserName =", value, "mqUserName");
            return (Criteria) this;
        }

        public Criteria andMqUserNameNotEqualTo(String value) {
            addCriterion("mqUserName <>", value, "mqUserName");
            return (Criteria) this;
        }

        public Criteria andMqUserNameGreaterThan(String value) {
            addCriterion("mqUserName >", value, "mqUserName");
            return (Criteria) this;
        }

        public Criteria andMqUserNameGreaterThanOrEqualTo(String value) {
            addCriterion("mqUserName >=", value, "mqUserName");
            return (Criteria) this;
        }

        public Criteria andMqUserNameLessThan(String value) {
            addCriterion("mqUserName <", value, "mqUserName");
            return (Criteria) this;
        }

        public Criteria andMqUserNameLessThanOrEqualTo(String value) {
            addCriterion("mqUserName <=", value, "mqUserName");
            return (Criteria) this;
        }

        public Criteria andMqUserNameLike(String value) {
            addCriterion("mqUserName like", value, "mqUserName");
            return (Criteria) this;
        }

        public Criteria andMqUserNameNotLike(String value) {
            addCriterion("mqUserName not like", value, "mqUserName");
            return (Criteria) this;
        }

        public Criteria andMqUserNameIn(List<String> values) {
            addCriterion("mqUserName in", values, "mqUserName");
            return (Criteria) this;
        }

        public Criteria andMqUserNameNotIn(List<String> values) {
            addCriterion("mqUserName not in", values, "mqUserName");
            return (Criteria) this;
        }

        public Criteria andMqUserNameBetween(String value1, String value2) {
            addCriterion("mqUserName between", value1, value2, "mqUserName");
            return (Criteria) this;
        }

        public Criteria andMqUserNameNotBetween(String value1, String value2) {
            addCriterion("mqUserName not between", value1, value2, "mqUserName");
            return (Criteria) this;
        }

        public Criteria andMqPasswdIsNull() {
            addCriterion("mqPasswd is null");
            return (Criteria) this;
        }

        public Criteria andMqPasswdIsNotNull() {
            addCriterion("mqPasswd is not null");
            return (Criteria) this;
        }

        public Criteria andMqPasswdEqualTo(String value) {
            addCriterion("mqPasswd =", value, "mqPasswd");
            return (Criteria) this;
        }

        public Criteria andMqPasswdNotEqualTo(String value) {
            addCriterion("mqPasswd <>", value, "mqPasswd");
            return (Criteria) this;
        }

        public Criteria andMqPasswdGreaterThan(String value) {
            addCriterion("mqPasswd >", value, "mqPasswd");
            return (Criteria) this;
        }

        public Criteria andMqPasswdGreaterThanOrEqualTo(String value) {
            addCriterion("mqPasswd >=", value, "mqPasswd");
            return (Criteria) this;
        }

        public Criteria andMqPasswdLessThan(String value) {
            addCriterion("mqPasswd <", value, "mqPasswd");
            return (Criteria) this;
        }

        public Criteria andMqPasswdLessThanOrEqualTo(String value) {
            addCriterion("mqPasswd <=", value, "mqPasswd");
            return (Criteria) this;
        }

        public Criteria andMqPasswdLike(String value) {
            addCriterion("mqPasswd like", value, "mqPasswd");
            return (Criteria) this;
        }

        public Criteria andMqPasswdNotLike(String value) {
            addCriterion("mqPasswd not like", value, "mqPasswd");
            return (Criteria) this;
        }

        public Criteria andMqPasswdIn(List<String> values) {
            addCriterion("mqPasswd in", values, "mqPasswd");
            return (Criteria) this;
        }

        public Criteria andMqPasswdNotIn(List<String> values) {
            addCriterion("mqPasswd not in", values, "mqPasswd");
            return (Criteria) this;
        }

        public Criteria andMqPasswdBetween(String value1, String value2) {
            addCriterion("mqPasswd between", value1, value2, "mqPasswd");
            return (Criteria) this;
        }

        public Criteria andMqPasswdNotBetween(String value1, String value2) {
            addCriterion("mqPasswd not between", value1, value2, "mqPasswd");
            return (Criteria) this;
        }

        public Criteria andMqTopicNameIsNull() {
            addCriterion("mqTopicName is null");
            return (Criteria) this;
        }

        public Criteria andMqTopicNameIsNotNull() {
            addCriterion("mqTopicName is not null");
            return (Criteria) this;
        }

        public Criteria andMqTopicNameEqualTo(String value) {
            addCriterion("mqTopicName =", value, "mqTopicName");
            return (Criteria) this;
        }

        public Criteria andMqTopicNameNotEqualTo(String value) {
            addCriterion("mqTopicName <>", value, "mqTopicName");
            return (Criteria) this;
        }

        public Criteria andMqTopicNameGreaterThan(String value) {
            addCriterion("mqTopicName >", value, "mqTopicName");
            return (Criteria) this;
        }

        public Criteria andMqTopicNameGreaterThanOrEqualTo(String value) {
            addCriterion("mqTopicName >=", value, "mqTopicName");
            return (Criteria) this;
        }

        public Criteria andMqTopicNameLessThan(String value) {
            addCriterion("mqTopicName <", value, "mqTopicName");
            return (Criteria) this;
        }

        public Criteria andMqTopicNameLessThanOrEqualTo(String value) {
            addCriterion("mqTopicName <=", value, "mqTopicName");
            return (Criteria) this;
        }

        public Criteria andMqTopicNameLike(String value) {
            addCriterion("mqTopicName like", value, "mqTopicName");
            return (Criteria) this;
        }

        public Criteria andMqTopicNameNotLike(String value) {
            addCriterion("mqTopicName not like", value, "mqTopicName");
            return (Criteria) this;
        }

        public Criteria andMqTopicNameIn(List<String> values) {
            addCriterion("mqTopicName in", values, "mqTopicName");
            return (Criteria) this;
        }

        public Criteria andMqTopicNameNotIn(List<String> values) {
            addCriterion("mqTopicName not in", values, "mqTopicName");
            return (Criteria) this;
        }

        public Criteria andMqTopicNameBetween(String value1, String value2) {
            addCriterion("mqTopicName between", value1, value2, "mqTopicName");
            return (Criteria) this;
        }

        public Criteria andMqTopicNameNotBetween(String value1, String value2) {
            addCriterion("mqTopicName not between", value1, value2, "mqTopicName");
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

        public Criteria andWeightIsNull() {
            addCriterion("weight is null");
            return (Criteria) this;
        }

        public Criteria andWeightIsNotNull() {
            addCriterion("weight is not null");
            return (Criteria) this;
        }

        public Criteria andWeightEqualTo(Integer value) {
            addCriterion("weight =", value, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightNotEqualTo(Integer value) {
            addCriterion("weight <>", value, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightGreaterThan(Integer value) {
            addCriterion("weight >", value, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightGreaterThanOrEqualTo(Integer value) {
            addCriterion("weight >=", value, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightLessThan(Integer value) {
            addCriterion("weight <", value, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightLessThanOrEqualTo(Integer value) {
            addCriterion("weight <=", value, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightIn(List<Integer> values) {
            addCriterion("weight in", values, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightNotIn(List<Integer> values) {
            addCriterion("weight not in", values, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightBetween(Integer value1, Integer value2) {
            addCriterion("weight between", value1, value2, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightNotBetween(Integer value1, Integer value2) {
            addCriterion("weight not between", value1, value2, "weight");
            return (Criteria) this;
        }

        public Criteria andChargeIsNull() {
            addCriterion("charge is null");
            return (Criteria) this;
        }

        public Criteria andChargeIsNotNull() {
            addCriterion("charge is not null");
            return (Criteria) this;
        }

        public Criteria andChargeEqualTo(Integer value) {
            addCriterion("charge =", value, "charge");
            return (Criteria) this;
        }

        public Criteria andChargeNotEqualTo(Integer value) {
            addCriterion("charge <>", value, "charge");
            return (Criteria) this;
        }

        public Criteria andChargeGreaterThan(Integer value) {
            addCriterion("charge >", value, "charge");
            return (Criteria) this;
        }

        public Criteria andChargeGreaterThanOrEqualTo(Integer value) {
            addCriterion("charge >=", value, "charge");
            return (Criteria) this;
        }

        public Criteria andChargeLessThan(Integer value) {
            addCriterion("charge <", value, "charge");
            return (Criteria) this;
        }

        public Criteria andChargeLessThanOrEqualTo(Integer value) {
            addCriterion("charge <=", value, "charge");
            return (Criteria) this;
        }

        public Criteria andChargeIn(List<Integer> values) {
            addCriterion("charge in", values, "charge");
            return (Criteria) this;
        }

        public Criteria andChargeNotIn(List<Integer> values) {
            addCriterion("charge not in", values, "charge");
            return (Criteria) this;
        }

        public Criteria andChargeBetween(Integer value1, Integer value2) {
            addCriterion("charge between", value1, value2, "charge");
            return (Criteria) this;
        }

        public Criteria andChargeNotBetween(Integer value1, Integer value2) {
            addCriterion("charge not between", value1, value2, "charge");
            return (Criteria) this;
        }

        public Criteria andUniqueUuidIsNull() {
            addCriterion("uniqueUuid is null");
            return (Criteria) this;
        }

        public Criteria andUniqueUuidIsNotNull() {
            addCriterion("uniqueUuid is not null");
            return (Criteria) this;
        }

        public Criteria andUniqueUuidEqualTo(String value) {
            addCriterion("uniqueUuid =", value, "uniqueUuid");
            return (Criteria) this;
        }

        public Criteria andUniqueUuidNotEqualTo(String value) {
            addCriterion("uniqueUuid <>", value, "uniqueUuid");
            return (Criteria) this;
        }

        public Criteria andUniqueUuidGreaterThan(String value) {
            addCriterion("uniqueUuid >", value, "uniqueUuid");
            return (Criteria) this;
        }

        public Criteria andUniqueUuidGreaterThanOrEqualTo(String value) {
            addCriterion("uniqueUuid >=", value, "uniqueUuid");
            return (Criteria) this;
        }

        public Criteria andUniqueUuidLessThan(String value) {
            addCriterion("uniqueUuid <", value, "uniqueUuid");
            return (Criteria) this;
        }

        public Criteria andUniqueUuidLessThanOrEqualTo(String value) {
            addCriterion("uniqueUuid <=", value, "uniqueUuid");
            return (Criteria) this;
        }

        public Criteria andUniqueUuidLike(String value) {
            addCriterion("uniqueUuid like", value, "uniqueUuid");
            return (Criteria) this;
        }

        public Criteria andUniqueUuidNotLike(String value) {
            addCriterion("uniqueUuid not like", value, "uniqueUuid");
            return (Criteria) this;
        }

        public Criteria andUniqueUuidIn(List<String> values) {
            addCriterion("uniqueUuid in", values, "uniqueUuid");
            return (Criteria) this;
        }

        public Criteria andUniqueUuidNotIn(List<String> values) {
            addCriterion("uniqueUuid not in", values, "uniqueUuid");
            return (Criteria) this;
        }

        public Criteria andUniqueUuidBetween(String value1, String value2) {
            addCriterion("uniqueUuid between", value1, value2, "uniqueUuid");
            return (Criteria) this;
        }

        public Criteria andUniqueUuidNotBetween(String value1, String value2) {
            addCriterion("uniqueUuid not between", value1, value2, "uniqueUuid");
            return (Criteria) this;
        }

        public Criteria andLimitStrategyuuidIsNull() {
            addCriterion("limitStrategyuuid is null");
            return (Criteria) this;
        }

        public Criteria andLimitStrategyuuidIsNotNull() {
            addCriterion("limitStrategyuuid is not null");
            return (Criteria) this;
        }

        public Criteria andLimitStrategyuuidEqualTo(String value) {
            addCriterion("limitStrategyuuid =", value, "limitStrategyuuid");
            return (Criteria) this;
        }

        public Criteria andLimitStrategyuuidNotEqualTo(String value) {
            addCriterion("limitStrategyuuid <>", value, "limitStrategyuuid");
            return (Criteria) this;
        }

        public Criteria andLimitStrategyuuidGreaterThan(String value) {
            addCriterion("limitStrategyuuid >", value, "limitStrategyuuid");
            return (Criteria) this;
        }

        public Criteria andLimitStrategyuuidGreaterThanOrEqualTo(String value) {
            addCriterion("limitStrategyuuid >=", value, "limitStrategyuuid");
            return (Criteria) this;
        }

        public Criteria andLimitStrategyuuidLessThan(String value) {
            addCriterion("limitStrategyuuid <", value, "limitStrategyuuid");
            return (Criteria) this;
        }

        public Criteria andLimitStrategyuuidLessThanOrEqualTo(String value) {
            addCriterion("limitStrategyuuid <=", value, "limitStrategyuuid");
            return (Criteria) this;
        }

        public Criteria andLimitStrategyuuidLike(String value) {
            addCriterion("limitStrategyuuid like", value, "limitStrategyuuid");
            return (Criteria) this;
        }

        public Criteria andLimitStrategyuuidNotLike(String value) {
            addCriterion("limitStrategyuuid not like", value, "limitStrategyuuid");
            return (Criteria) this;
        }

        public Criteria andLimitStrategyuuidIn(List<String> values) {
            addCriterion("limitStrategyuuid in", values, "limitStrategyuuid");
            return (Criteria) this;
        }

        public Criteria andLimitStrategyuuidNotIn(List<String> values) {
            addCriterion("limitStrategyuuid not in", values, "limitStrategyuuid");
            return (Criteria) this;
        }

        public Criteria andLimitStrategyuuidBetween(String value1, String value2) {
            addCriterion("limitStrategyuuid between", value1, value2, "limitStrategyuuid");
            return (Criteria) this;
        }

        public Criteria andLimitStrategyuuidNotBetween(String value1, String value2) {
            addCriterion("limitStrategyuuid not between", value1, value2, "limitStrategyuuid");
            return (Criteria) this;
        }

        public Criteria andInterfaceNameIsNull() {
            addCriterion("interfaceName is null");
            return (Criteria) this;
        }

        public Criteria andInterfaceNameIsNotNull() {
            addCriterion("interfaceName is not null");
            return (Criteria) this;
        }

        public Criteria andInterfaceNameEqualTo(String value) {
            addCriterion("interfaceName =", value, "interfaceName");
            return (Criteria) this;
        }

        public Criteria andInterfaceNameNotEqualTo(String value) {
            addCriterion("interfaceName <>", value, "interfaceName");
            return (Criteria) this;
        }

        public Criteria andInterfaceNameGreaterThan(String value) {
            addCriterion("interfaceName >", value, "interfaceName");
            return (Criteria) this;
        }

        public Criteria andInterfaceNameGreaterThanOrEqualTo(String value) {
            addCriterion("interfaceName >=", value, "interfaceName");
            return (Criteria) this;
        }

        public Criteria andInterfaceNameLessThan(String value) {
            addCriterion("interfaceName <", value, "interfaceName");
            return (Criteria) this;
        }

        public Criteria andInterfaceNameLessThanOrEqualTo(String value) {
            addCriterion("interfaceName <=", value, "interfaceName");
            return (Criteria) this;
        }

        public Criteria andInterfaceNameLike(String value) {
            addCriterion("interfaceName like", value, "interfaceName");
            return (Criteria) this;
        }

        public Criteria andInterfaceNameNotLike(String value) {
            addCriterion("interfaceName not like", value, "interfaceName");
            return (Criteria) this;
        }

        public Criteria andInterfaceNameIn(List<String> values) {
            addCriterion("interfaceName in", values, "interfaceName");
            return (Criteria) this;
        }

        public Criteria andInterfaceNameNotIn(List<String> values) {
            addCriterion("interfaceName not in", values, "interfaceName");
            return (Criteria) this;
        }

        public Criteria andInterfaceNameBetween(String value1, String value2) {
            addCriterion("interfaceName between", value1, value2, "interfaceName");
            return (Criteria) this;
        }

        public Criteria andInterfaceNameNotBetween(String value1, String value2) {
            addCriterion("interfaceName not between", value1, value2, "interfaceName");
            return (Criteria) this;
        }

        public Criteria andBackendProtocolTypeIsNull() {
            addCriterion("backendProtocolType is null");
            return (Criteria) this;
        }

        public Criteria andBackendProtocolTypeIsNotNull() {
            addCriterion("backendProtocolType is not null");
            return (Criteria) this;
        }

        public Criteria andBackendProtocolTypeEqualTo(Integer value) {
            addCriterion("backendProtocolType =", value, "backendProtocolType");
            return (Criteria) this;
        }

        public Criteria andBackendProtocolTypeNotEqualTo(Integer value) {
            addCriterion("backendProtocolType <>", value, "backendProtocolType");
            return (Criteria) this;
        }

        public Criteria andBackendProtocolTypeGreaterThan(Integer value) {
            addCriterion("backendProtocolType >", value, "backendProtocolType");
            return (Criteria) this;
        }

        public Criteria andBackendProtocolTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("backendProtocolType >=", value, "backendProtocolType");
            return (Criteria) this;
        }

        public Criteria andBackendProtocolTypeLessThan(Integer value) {
            addCriterion("backendProtocolType <", value, "backendProtocolType");
            return (Criteria) this;
        }

        public Criteria andBackendProtocolTypeLessThanOrEqualTo(Integer value) {
            addCriterion("backendProtocolType <=", value, "backendProtocolType");
            return (Criteria) this;
        }

        public Criteria andBackendProtocolTypeIn(List<Integer> values) {
            addCriterion("backendProtocolType in", values, "backendProtocolType");
            return (Criteria) this;
        }

        public Criteria andBackendProtocolTypeNotIn(List<Integer> values) {
            addCriterion("backendProtocolType not in", values, "backendProtocolType");
            return (Criteria) this;
        }

        public Criteria andBackendProtocolTypeBetween(Integer value1, Integer value2) {
            addCriterion("backendProtocolType between", value1, value2, "backendProtocolType");
            return (Criteria) this;
        }

        public Criteria andBackendProtocolTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("backendProtocolType not between", value1, value2, "backendProtocolType");
            return (Criteria) this;
        }

        public Criteria andCacheUnitIsNull() {
            addCriterion("cacheUnit is null");
            return (Criteria) this;
        }

        public Criteria andCacheUnitIsNotNull() {
            addCriterion("cacheUnit is not null");
            return (Criteria) this;
        }

        public Criteria andCacheUnitEqualTo(Integer value) {
            addCriterion("cacheUnit =", value, "cacheUnit");
            return (Criteria) this;
        }

        public Criteria andCacheUnitNotEqualTo(Integer value) {
            addCriterion("cacheUnit <>", value, "cacheUnit");
            return (Criteria) this;
        }

        public Criteria andCacheUnitGreaterThan(Integer value) {
            addCriterion("cacheUnit >", value, "cacheUnit");
            return (Criteria) this;
        }

        public Criteria andCacheUnitGreaterThanOrEqualTo(Integer value) {
            addCriterion("cacheUnit >=", value, "cacheUnit");
            return (Criteria) this;
        }

        public Criteria andCacheUnitLessThan(Integer value) {
            addCriterion("cacheUnit <", value, "cacheUnit");
            return (Criteria) this;
        }

        public Criteria andCacheUnitLessThanOrEqualTo(Integer value) {
            addCriterion("cacheUnit <=", value, "cacheUnit");
            return (Criteria) this;
        }

        public Criteria andCacheUnitIn(List<Integer> values) {
            addCriterion("cacheUnit in", values, "cacheUnit");
            return (Criteria) this;
        }

        public Criteria andCacheUnitNotIn(List<Integer> values) {
            addCriterion("cacheUnit not in", values, "cacheUnit");
            return (Criteria) this;
        }

        public Criteria andCacheUnitBetween(Integer value1, Integer value2) {
            addCriterion("cacheUnit between", value1, value2, "cacheUnit");
            return (Criteria) this;
        }

        public Criteria andCacheUnitNotBetween(Integer value1, Integer value2) {
            addCriterion("cacheUnit not between", value1, value2, "cacheUnit");
            return (Criteria) this;
        }

        public Criteria andCacheNoIsNull() {
            addCriterion("cacheNo is null");
            return (Criteria) this;
        }

        public Criteria andCacheNoIsNotNull() {
            addCriterion("cacheNo is not null");
            return (Criteria) this;
        }

        public Criteria andCacheNoEqualTo(Integer value) {
            addCriterion("cacheNo =", value, "cacheNo");
            return (Criteria) this;
        }

        public Criteria andCacheNoNotEqualTo(Integer value) {
            addCriterion("cacheNo <>", value, "cacheNo");
            return (Criteria) this;
        }

        public Criteria andCacheNoGreaterThan(Integer value) {
            addCriterion("cacheNo >", value, "cacheNo");
            return (Criteria) this;
        }

        public Criteria andCacheNoGreaterThanOrEqualTo(Integer value) {
            addCriterion("cacheNo >=", value, "cacheNo");
            return (Criteria) this;
        }

        public Criteria andCacheNoLessThan(Integer value) {
            addCriterion("cacheNo <", value, "cacheNo");
            return (Criteria) this;
        }

        public Criteria andCacheNoLessThanOrEqualTo(Integer value) {
            addCriterion("cacheNo <=", value, "cacheNo");
            return (Criteria) this;
        }

        public Criteria andCacheNoIn(List<Integer> values) {
            addCriterion("cacheNo in", values, "cacheNo");
            return (Criteria) this;
        }

        public Criteria andCacheNoNotIn(List<Integer> values) {
            addCriterion("cacheNo not in", values, "cacheNo");
            return (Criteria) this;
        }

        public Criteria andCacheNoBetween(Integer value1, Integer value2) {
            addCriterion("cacheNo between", value1, value2, "cacheNo");
            return (Criteria) this;
        }

        public Criteria andCacheNoNotBetween(Integer value1, Integer value2) {
            addCriterion("cacheNo not between", value1, value2, "cacheNo");
            return (Criteria) this;
        }


        public Criteria andLimitStrategyTotalIsNull() {
            addCriterion("limitStrategyTotal is null");
            return (Criteria) this;
        }

        public Criteria andLimitStrategyTotalIsNotNull() {
            addCriterion("limitStrategyTotal is not null");
            return (Criteria) this;
        }

        public Criteria andLimitStrategyTotalEqualTo(Integer value) {
            addCriterion("limitStrategyTotal =", value, "limitStrategyTotal");
            return (Criteria) this;
        }

        public Criteria andLimitStrategyTotalNotEqualTo(Integer value) {
            addCriterion("limitStrategyTotal <>", value, "limitStrategyTotal");
            return (Criteria) this;
        }

        public Criteria andLimitStrategyTotalGreaterThan(Integer value) {
            addCriterion("limitStrategyTotal >", value, "limitStrategyTotal");
            return (Criteria) this;
        }

        public Criteria andLimitStrategyTotalGreaterThanOrEqualTo(Integer value) {
            addCriterion("limitStrategyTotal >=", value, "limitStrategyTotal");
            return (Criteria) this;
        }

        public Criteria andLimitStrategyTotalLessThan(Integer value) {
            addCriterion("limitStrategyTotal <", value, "limitStrategyTotal");
            return (Criteria) this;
        }

        public Criteria andLimitStrategyTotalLessThanOrEqualTo(Integer value) {
            addCriterion("limitStrategyTotal <=", value, "limitStrategyTotal");
            return (Criteria) this;
        }

        public Criteria andLimitStrategyTotalIn(List<Integer> values) {
            addCriterion("limitStrategyTotal in", values, "limitStrategyTotal");
            return (Criteria) this;
        }

        public Criteria andLimitStrategyTotalNotIn(List<Integer> values) {
            addCriterion("limitStrategyTotal not in", values, "limitStrategyTotal");
            return (Criteria) this;
        }

        public Criteria andLimitStrategyTotalBetween(Integer value1, Integer value2) {
            addCriterion("limitStrategyTotal between", value1, value2, "limitStrategyTotal");
            return (Criteria) this;
        }

        public Criteria andLimitStrategyTotalNotBetween(Integer value1, Integer value2) {
            addCriterion("limitStrategyTotal not between", value1, value2, "limitStrategyTotal");
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
