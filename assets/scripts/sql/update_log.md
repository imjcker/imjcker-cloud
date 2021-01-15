#2020-04-01  新建两张表  qiuwen
```sql
USE gateway_system;

CREATE TABLE `auth_stock_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `api_id` int(11) DEFAULT NULL COMMENT '接口id',
  `app_key` varchar(256) DEFAULT NULL COMMENT '客户',
  `stock` int(11) DEFAULT NULL COMMENT '余量',
  `create_time` bigint(13) DEFAULT NULL COMMENT '日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='接口调用记录';

CREATE TABLE `company_balance_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_key` varchar(256) DEFAULT NULL COMMENT '客户',
  `balance` decimal(10,2) DEFAULT NULL COMMENT '余额',
  `stock` int(11) DEFAULT NULL COMMENT '余量',
  `create_time` bigint(13) DEFAULT NULL COMMENT '日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='客户余额余量记录';

```

#2020-04-01  引入tk-mybatis  同步配置文件  目前只针对charge服务  mybatis块修改 qiuwen
```xml script
mybatis:
  mapperLocations: classpath:mybatis/mapper/*.xml
  typeAliasPackage: com.lemon.*.*.po
  configuration:
    localCacheScope: statement
    mapUnderscoreToCamelCase: true
mapper:
  notEmpty: false
  identity: MYSQL
```

#tk-修改表信息 2020-4-8
```sql
#修改字段billing_cycle_limit 为bigint
ALTER TABLE `billing_rules`
MODIFY COLUMN `billing_cycle_limit`  bigint(11) NULL DEFAULT NULL COMMENT '时间计费时生效，限定周期内的最大调用量，-1表示不限量' AFTER `billing_cycle`;

#给定字段billing_mode 默认值1
ALTER TABLE `billing_rules`
MODIFY COLUMN `billing_mode`  int(1) NULL DEFAULT 1 COMMENT '计费模式(默认1查询计费，2查得计费)' AFTER `billing_cycle_limit`;

#修改company_bill_day_datail表字段为下划线
ALTER TABLE `company_bill_day_datail`
CHANGE COLUMN `appKey` `app_key`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户标识' AFTER `id`,
CHANGE COLUMN `apiId` `api_id`  int(11) NULL DEFAULT NULL COMMENT '接口id' AFTER `app_key`,
CHANGE COLUMN `billingRulesUuid` `billing_rules_uuid`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '规则uuid' AFTER `api_id`,
CHANGE COLUMN `createTime` `create_time`  bigint(13) NULL DEFAULT NULL AFTER `amount`;

#修改表字段格式 company_bill_day
ALTER TABLE `company_bill_day`
CHANGE COLUMN `appKey` `app_key`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户标识' AFTER `id`,
CHANGE COLUMN `createTime` `create_time`  bigint(13) NOT NULL COMMENT '日期-天' AFTER `balance`;

#修改表字段格式 company_bill_month
ALTER TABLE `company_bill_month`
CHANGE COLUMN `appKey` `app_key`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户标识' AFTER `id`,
CHANGE COLUMN `createTime` `create_time`  bigint(13) NULL DEFAULT NULL COMMENT '日期-月份' AFTER `balance`;

#修改表字段格式 company_bill_year
ALTER TABLE `company_bill_year`
CHANGE COLUMN `appKey` `app_key`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户标识' AFTER `id`,
CHANGE COLUMN `createTime` `create_time`  bigint(13) NOT NULL COMMENT '日期-月份' AFTER `balance`;

```

#tk-修改表信息 2020-4-9
```sql
#修改表字段 company_apps_auth
ALTER TABLE `company_apps_auth`
CHANGE COLUMN `apiId` `api_id`  int(11) NOT NULL COMMENT '接口id' AFTER `id`,
CHANGE COLUMN `appKey` `app_key`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户标识' AFTER `api_id`,
CHANGE COLUMN `startTime` `start_time`  bigint(13) NOT NULL COMMENT '接口调用有效起始时间' AFTER `app_key`,
CHANGE COLUMN `endTime` `end_time`  bigint(13) NOT NULL COMMENT '接口调用终止时间' AFTER `start_time`,
CHANGE COLUMN `createTime` `create_time`  bigint(13) NOT NULL COMMENT '创建时间' AFTER `end_time`,
CHANGE COLUMN `updateTime` `update_time`  bigint(13) NOT NULL COMMENT '更新时间' AFTER `create_time`,
MODIFY COLUMN `status`  int(1) NOT NULL DEFAULT 1 COMMENT '状态码,1启用,0禁用' AFTER `update_time`;

#修改表字段 company_apps_auth_version
ALTER TABLE `company_apps_auth_version`
CHANGE COLUMN `apiId` `api_id`  int(11) NULL DEFAULT NULL AFTER `id`;

```

#Exit新增kafka topic
```yml
kafka:
  exitTopicName: inmgr-flink-interfaceLogInfo
```

#2020-04-22 新增commany_app表字段
```sql
ALTER TABLE `company_apps`
ADD COLUMN `public_key`  varchar(512) NOT NULL COMMENT '用户公钥' AFTER `price`,
ADD COLUMN `private_key`  varchar(1024) NOT NULL COMMENT '用户私钥' AFTER `public_key`;

```

# 2020-05-09 修改call_back_param, 更改parent_id类型为int
```sql
update call_back_param set parent_id = "0" where parent_id = "" ;
update call_back_param set parent_id = "0" where parent_id is null ;
ALTER TABLE call_back_param MODIFY parent_id int(11);
```

# 2020-05-12 
```sql
ALTER TABLE `company_balance_history`
ADD COLUMN `record_time`  date NOT NULL COMMENT '记录统计的时间' AFTER `create_time`;

ALTER TABLE `auth_stock_history`
ADD COLUMN `record_time`  date NOT NULL COMMENT '记录统计的时间' AFTER `create_time`;

```