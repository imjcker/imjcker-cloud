package com.imjcker.api.handler.po;

import java.util.ArrayList;
import java.util.List;

public class ApiRateDistributeExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ApiRateDistributeExample() {
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
