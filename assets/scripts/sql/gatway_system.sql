/*
Navicat MySQL Data Transfer

Source Server         : 90环境
Source Server Version : 50627
Source Host           : 172.32.4.89:3306
Source Database       : gateway_system

Target Server Type    : MYSQL
Target Server Version : 50627
File Encoding         : 65001

Date: 2020-04-27 08:53:19
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for agency_api_account
-- ----------------------------
DROP TABLE IF EXISTS `agency_api_account`;
CREATE TABLE `agency_api_account` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增 id',
  `apiId` int(11) DEFAULT NULL COMMENT '接口id',
  `apiName` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '接口名称',
  `apiGroupId` int(11) NOT NULL COMMENT 'api分组 Id',
  `apiGroupName` varchar(255) DEFAULT NULL COMMENT '分组名称',
  `appKey` varchar(255) NOT NULL COMMENT '客户appKey',
  `dataConfig` mediumtext CHARACTER SET utf16le COMMENT '账户信息',
  `sourceFlag` int(1) DEFAULT NULL COMMENT '是否分组账户 0是  1不是',
  `status` int(1) DEFAULT NULL COMMENT '状态码,1:代表可用,2代表不可用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 COMMENT='机构账户信息表';

-- ----------------------------
-- Table structure for api_app_relation
-- ----------------------------
DROP TABLE IF EXISTS `api_app_relation`;
CREATE TABLE `api_app_relation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `apiId` int(11) DEFAULT NULL COMMENT 'apiId',
  `appCertificationId` int(11) DEFAULT NULL COMMENT 'app应用 Id',
  `env` int(1) DEFAULT NULL COMMENT '1:代表生产环境,2:代表测试环境',
  `status` int(1) DEFAULT '1' COMMENT '状态码. 1:代表可用, 2:代表不可用',
  `createTime` bigint(13) DEFAULT NULL COMMENT '创建时间',
  `updateTime` bigint(13) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000;

-- ----------------------------
-- Table structure for api_combination
-- ----------------------------
DROP TABLE IF EXISTS `api_combination`;
CREATE TABLE `api_combination` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增 id',
  `combinationId` int(11) DEFAULT NULL COMMENT '组合接口id',
  `apiGroupId` int(11) DEFAULT NULL COMMENT 'api分组 Id',
  `apiId` int(11) DEFAULT NULL COMMENT 'url路径',
  `apiName` varchar(255) DEFAULT NULL COMMENT '接口名称',
  `httpPath` text COMMENT 'API描述信息,需判断最大2000字',
  `weight` int(3) DEFAULT NULL,
  `score` double(11,2) DEFAULT NULL,
  `status` int(1) DEFAULT NULL COMMENT '状态码,1:代表可用,2代表不可用,3代表下线',
  `createTime` bigint(13) DEFAULT NULL COMMENT 'api创建时间',
  `updateTime` bigint(13) DEFAULT NULL COMMENT 'api 更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000;

-- ----------------------------
-- Table structure for api_count
-- ----------------------------
DROP TABLE IF EXISTS `api_count`;
CREATE TABLE `api_count` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `api_group_id` int(11) NOT NULL,
  `api_id` int(11) NOT NULL,
  `api_name` varchar(255) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `avg_success_response_time` int(11) NOT NULL,
  `count` bigint(20) NOT NULL,
  `count_fail` bigint(20) NOT NULL,
  `count_success` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000;

-- ----------------------------
-- Table structure for api_count_by_app_key
-- ----------------------------
DROP TABLE IF EXISTS `api_count_by_app_key`;
CREATE TABLE `api_count_by_app_key` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `api_id` int(11) NOT NULL,
  `api_name` varchar(255) DEFAULT NULL,
  `app_key` varchar(255) DEFAULT NULL,
  `avg_success_response_time` int(11) NOT NULL,
  `count` bigint(20) NOT NULL,
  `count_fail` bigint(20) NOT NULL,
  `count_success` bigint(20) NOT NULL,
  `date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000;

-- ----------------------------
-- Table structure for api_group
-- ----------------------------
DROP TABLE IF EXISTS `api_group`;
CREATE TABLE `api_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `groupUUID` varchar(64) DEFAULT NULL COMMENT '分组的唯一uuid,使用 uuid 自动生成,去掉 uuid 中间的-符号',
  `groupName` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '分组名称',
  `groupDomainName` varchar(255) DEFAULT 'openapi.lemoncome.com' COMMENT '分组绑定的域名',
  `groupDescription` varchar(255) DEFAULT NULL COMMENT '分组描述信息',
  `status` int(2) DEFAULT '1' COMMENT '分组状态,1:正常,2:删除',
  `createTime` bigint(13) DEFAULT NULL COMMENT '创建时间',
  `updateTime` bigint(13) DEFAULT NULL COMMENT '更新时间',
  `groupLevel` int(4) DEFAULT NULL COMMENT '树层级',
  `parentId` int(4) DEFAULT NULL COMMENT '父级ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000;

-- ----------------------------
-- Table structure for api_group_relation
-- ----------------------------
DROP TABLE IF EXISTS `api_group_relation`;
CREATE TABLE `api_group_relation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `apiId` int(11) NOT NULL COMMENT 'API的apiId',
  `groupId` int(11) NOT NULL COMMENT 'group表的id',
  `path` varchar(50) DEFAULT NULL COMMENT '全路径',
  `createTime` bigint(13) DEFAULT NULL COMMENT '创建时间',
  `fullPathName` varchar(200) DEFAULT NULL COMMENT '节点的路径全称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 COMMENT='api与group的关系表';

-- ----------------------------
-- Table structure for api_info
-- ----------------------------
DROP TABLE IF EXISTS `api_info`;
CREATE TABLE `api_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增 id',
  `apiName` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'api 名称',
  `apiGroupId` int(11) DEFAULT NULL COMMENT 'api分组 Id,不同于 uuid',
  `httpPath` varchar(255) DEFAULT NULL COMMENT 'url路径',
  `httpMethod` int(1) DEFAULT NULL COMMENT '1:代表get 方式,2:代表 post 方式',
  `apiDescription` text COMMENT 'API描述信息,需判断最大2000字',
  `backEndAddress` varchar(255) DEFAULT NULL COMMENT '后台第三方请求地址',
  `backEndAddressB` varchar(255) DEFAULT '',
  `backEndPath` varchar(255) DEFAULT NULL COMMENT '后台请求第三方的路径地址,例如: /listUser, 此值必须以/开始',
  `backEndHttpMethod` int(1) DEFAULT NULL COMMENT '后端请求方式,1:get,2:post',
  `backEndTimeout` int(5) DEFAULT NULL COMMENT '设置后台超时时间, connectiontimeout,readtimeout,sockettimeout,最大值30000,单位毫秒',
  `isMock` int(1) DEFAULT '1' COMMENT '是否使用 mock 数据, 1:使用,2:不使用.',
  `mockData` text COMMENT '如果使用了 mock 数据,那么必须要填写 mock 数据值,用户自己输入.',
  `callBackType` int(1) DEFAULT '1' COMMENT '返回信息类型, 1:json',
  `callBackSuccessExample` text,
  `callBackFailExample` text,
  `publishProductEnvStatus` int(1) DEFAULT '2' COMMENT '是否发布生产环境, 1:发布,2:不发布',
  `productEnvVersion` varchar(255) DEFAULT NULL COMMENT '生产环境的发布版本号',
  `publishTestEnvStatus` int(1) DEFAULT '2' COMMENT '是否发布测试环境, 1:发布,2:不发布',
  `testEnvVersion` varchar(255) DEFAULT NULL COMMENT '测试环境的发布版本版本号',
  `saveMongoDB` int(1) DEFAULT NULL COMMENT '是否保存 mongodb,1:保存,2:不保存',
  `mongodbURI` varchar(255) DEFAULT NULL COMMENT '是否保存 mongodb的URI',
  `mongodbDBName` varchar(255) DEFAULT NULL COMMENT 'mongodb数据库名称',
  `mongodbCollectionName` varchar(255) DEFAULT NULL COMMENT 'mongodb collectionName',
  `saveMQ` int(1) DEFAULT NULL COMMENT '是否保存 mq,1:保存,2:不保存',
  `mqType` int(11) DEFAULT NULL COMMENT '1:rabbbitmq,2:kafka',
  `interfaceName` varchar(100) DEFAULT NULL COMMENT '上游接口名称',
  `mqAddress` varchar(255) DEFAULT NULL COMMENT 'mq 的地址',
  `uniqueUuid` varchar(10) DEFAULT NULL COMMENT '上游接口的唯一标识',
  `mqUserName` varchar(255) DEFAULT NULL COMMENT 'mq 用户名',
  `mqPasswd` varchar(255) DEFAULT NULL COMMENT 'mq 密码',
  `mqTopicName` varchar(255) DEFAULT NULL COMMENT '队列名称',
  `status` int(1) DEFAULT NULL COMMENT '状态码,1:代表可用,2代表不可用',
  `createTime` bigint(13) DEFAULT NULL COMMENT 'api创建时间',
  `updateTime` bigint(13) DEFAULT NULL COMMENT 'api 更新时间',
  `weight` int(3) DEFAULT NULL COMMENT '权重 0 - 10',
  `limitStrategyuuid` varchar(64) DEFAULT NULL COMMENT '该用户绑定的限流策略 uuid',
  `charge` int(3) DEFAULT NULL COMMENT '是否计费 1：是 2：否',
  `backendProtocolType` int(2) DEFAULT NULL COMMENT '上游协议类型 1:http 2:https',
  `cacheUnit` int(1) DEFAULT NULL COMMENT '三方结果缓存时间单位 1:小时 2:天 3:不限',
  `cacheNo` int(10) DEFAULT NULL COMMENT '三方结果缓存时间',
  `responseTransParam` mediumtext COMMENT '响应字段需要转码的json集合',
  `responseConfigJson` mediumtext COMMENT '转换的原始json',
  `jsonConfig` mediumtext CHARACTER SET utf16le,
  `limitStrategyTotal` int(11) DEFAULT '-1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 COMMENT='API的总体信息';

-- ----------------------------
-- Table structure for api_info_copy
-- ----------------------------
DROP TABLE IF EXISTS `api_info_copy`;
CREATE TABLE `api_info_copy` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增 id',
  `apiName` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'api 名称',
  `apiGroupId` int(11) DEFAULT NULL COMMENT 'api分组 Id,不同于 uuid',
  `httpPath` varchar(255) DEFAULT NULL COMMENT 'url路径',
  `httpMethod` int(1) DEFAULT NULL COMMENT '1:代表get 方式,2:代表 post 方式',
  `apiDescription` text COMMENT 'API描述信息,需判断最大2000字',
  `backEndAddress` varchar(255) DEFAULT NULL COMMENT '后台第三方请求地址',
  `backEndPath` varchar(255) DEFAULT NULL COMMENT '后台请求第三方的路径地址,例如: /listUser, 此值必须以/开始',
  `backEndHttpMethod` int(1) DEFAULT NULL COMMENT '后端请求方式,1:get,2:post',
  `backEndTimeout` int(5) DEFAULT NULL COMMENT '设置后台超时时间, connectiontimeout,readtimeout,sockettimeout,最大值30000,单位毫秒',
  `isMock` int(1) DEFAULT '1' COMMENT '是否使用 mock 数据, 1:使用,2:不使用.',
  `mockData` text COMMENT '如果使用了 mock 数据,那么必须要填写 mock 数据值,用户自己输入.',
  `callBackType` int(1) DEFAULT '1' COMMENT '返回信息类型, 1:json',
  `callBackSuccessExample` text,
  `callBackFailExample` text,
  `publishProductEnvStatus` int(1) DEFAULT '2' COMMENT '是否发布生产环境, 1:发布,2:不发布',
  `productEnvVersion` varchar(255) DEFAULT NULL COMMENT '生产环境的发布版本号',
  `publishTestEnvStatus` int(1) DEFAULT '2' COMMENT '是否发布测试环境, 1:发布,2:不发布',
  `testEnvVersion` varchar(255) DEFAULT NULL COMMENT '测试环境的发布版本版本号',
  `saveMongoDB` int(1) DEFAULT NULL COMMENT '是否保存 mongodb,1:保存,2:不保存',
  `mongodbURI` varchar(255) DEFAULT NULL COMMENT '是否保存 mongodb的URI',
  `mongodbDBName` varchar(255) DEFAULT NULL COMMENT 'mongodb数据库名称',
  `mongodbCollectionName` varchar(255) DEFAULT NULL COMMENT 'mongodb collectionName',
  `saveMQ` int(1) DEFAULT NULL COMMENT '是否保存 mq,1:保存,2:不保存',
  `mqType` int(11) DEFAULT NULL COMMENT '1:rabbbitmq,2:kafka',
  `interfaceName` varchar(100) DEFAULT NULL COMMENT '上游接口名称',
  `mqAddress` varchar(255) DEFAULT NULL COMMENT 'mq 的地址',
  `uniqueUuid` varchar(10) DEFAULT NULL COMMENT '上游接口的唯一标识',
  `mqUserName` varchar(255) DEFAULT NULL COMMENT 'mq 用户名',
  `mqPasswd` varchar(255) DEFAULT NULL COMMENT 'mq 密码',
  `mqTopicName` varchar(255) DEFAULT NULL COMMENT '队列名称',
  `status` int(1) DEFAULT NULL COMMENT '状态码,1:代表可用,2代表不可用',
  `createTime` bigint(13) DEFAULT NULL COMMENT 'api创建时间',
  `updateTime` bigint(13) DEFAULT NULL COMMENT 'api 更新时间',
  `weight` int(3) DEFAULT NULL COMMENT '权重 0 - 10',
  `limitStrategyuuid` varchar(64) DEFAULT NULL COMMENT '该用户绑定的限流策略 uuid',
  `charge` int(3) DEFAULT NULL COMMENT '是否计费 1：是 2：否',
  `backendProtocolType` int(2) DEFAULT NULL COMMENT '上游协议类型 1:http 2:https',
  `cacheUnit` int(1) DEFAULT NULL COMMENT '三方结果缓存时间单位 1:小时 2:天 3:不限',
  `cacheNo` int(10) DEFAULT NULL COMMENT '三方结果缓存时间',
  `responseTransParam` mediumtext COMMENT '响应字段需要转码的json集合',
  `responseConfigJson` mediumtext COMMENT '转换的原始json',
  `jsonConfig` mediumtext CHARACTER SET utf16le,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 COMMENT='API的总体信息';

-- ----------------------------
-- Table structure for api_info_versions
-- ----------------------------
DROP TABLE IF EXISTS `api_info_versions`;
CREATE TABLE `api_info_versions` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `versionId` varchar(50) NOT NULL COMMENT 'version id',
  `apiId` int(11) NOT NULL COMMENT 'API的 id',
  `apiName` varchar(255) DEFAULT NULL COMMENT 'api 名称',
  `apiGroupId` int(11) DEFAULT NULL COMMENT 'api分组 Id,不同于 uuid',
  `httpPath` varchar(255) DEFAULT NULL COMMENT 'url路径',
  `httpMethod` int(1) DEFAULT NULL COMMENT '1:代表get 方式,2:代表 post 方式',
  `apiDescription` text COMMENT 'API描述信息,需判断最大2000字',
  `backEndAddress` varchar(255) DEFAULT NULL COMMENT '后台第三方请求地址',
  `backEndAddressB` varchar(255) DEFAULT '',
  `backEndPath` varchar(255) DEFAULT NULL COMMENT '后台请求第三方的路径地址,例如: /listUser, 此值必须以/开始',
  `backEndHttpMethod` int(1) DEFAULT NULL COMMENT '后端请求方式,1:get,2:post',
  `backEndTimeout` int(5) DEFAULT NULL COMMENT '设置后台超时时间, connectiontimeout,readtimeout,sockettimeout,最大值30000,单位毫秒',
  `isMock` int(1) DEFAULT NULL COMMENT '是否使用 mock 数据, 1:使用,2:不使用.',
  `mockData` text COMMENT '如果使用了 mock 数据,那么必须要填写 mock 数据值,用户自己输入.',
  `callBackType` int(1) DEFAULT NULL COMMENT '返回信息类型, 1:json',
  `callBackSuccessExample` text COMMENT '返回成功示例',
  `callBackFailExample` text COMMENT '返回失败示例',
  `env` int(1) DEFAULT NULL COMMENT '发布过的环境, 1:线上,2:测试',
  `currentVersion` int(1) DEFAULT NULL COMMENT '是否当前发布版本,1:是,2:不是',
  `pubDescription` varchar(255) DEFAULT NULL COMMENT '发布的描述信息',
  `saveMongoDB` int(1) DEFAULT NULL COMMENT '是否保存 mongodb,1:保存,2:不保存',
  `mongodbURI` varchar(255) DEFAULT NULL COMMENT '是否保存 mongodb的URI',
  `mongodbDBName` varchar(255) DEFAULT NULL COMMENT 'mongodb 数据库名称',
  `mongodbCollectionName` varchar(255) DEFAULT NULL COMMENT 'mongodb collection名称',
  `saveMQ` int(1) DEFAULT NULL COMMENT '是否保存 mq,1:保存,2:不保存',
  `mqType` int(1) DEFAULT NULL COMMENT '1:rabbbitmq,2:kafka',
  `mqAddress` varchar(255) DEFAULT NULL COMMENT 'mq 的地址',
  `mqUserName` varchar(255) DEFAULT NULL COMMENT 'mq 用户名',
  `mqPasswd` varchar(255) DEFAULT NULL COMMENT 'mq 密码',
  `mqTopicName` varchar(255) DEFAULT NULL COMMENT '队列名称',
  `createTime` bigint(13) DEFAULT NULL COMMENT 'api创建时间',
  `weight` int(3) DEFAULT NULL COMMENT '权重 0 - 10',
  `charge` int(3) DEFAULT NULL COMMENT '是否计费 1：是 2：否',
  `uniqueUuid` varchar(10) DEFAULT NULL COMMENT '上游接口的唯一标识',
  `limitStrategyuuid` varchar(64) DEFAULT NULL COMMENT '该用户绑定的限流策略 uuid',
  `interfaceName` varchar(100) DEFAULT NULL COMMENT '上游接口名称',
  `backendProtocolType` int(2) DEFAULT NULL COMMENT '上游协议类型 1:http 2:https',
  `cacheUnit` int(1) DEFAULT NULL COMMENT '三方结果缓存时间单位 1:小时 2:天 3:不限',
  `cacheNo` int(10) DEFAULT NULL COMMENT '三方结果缓存时间',
  `responseTransParam` mediumtext COMMENT '响应字段需要转码的json集合',
  `responseConfigJson` mediumtext COMMENT '转换的原始json',
  `jsonConfig` mediumtext,
  `limitStrategyTotal` int(11) DEFAULT '-1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `versionId` (`versionId`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 COMMENT='API的总体信息历史版本';

-- ----------------------------
-- Table structure for api_info_versions_latest
-- ----------------------------
DROP TABLE IF EXISTS `api_info_versions_latest`;
CREATE TABLE `api_info_versions_latest` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `versionId` varchar(50) NOT NULL COMMENT 'version id',
  `apiId` int(11) NOT NULL COMMENT 'API的 id',
  `apiName` varchar(255) DEFAULT NULL COMMENT 'api 名称',
  `apiGroupId` int(11) DEFAULT NULL COMMENT 'api分组 Id,不同于 uuid',
  `httpPath` varchar(255) DEFAULT NULL COMMENT 'url路径',
  `httpMethod` int(1) DEFAULT NULL COMMENT '1:代表get 方式,2:代表 post 方式',
  `apiDescription` text COMMENT 'API描述信息,需判断最大2000字',
  `backEndAddress` varchar(255) DEFAULT NULL COMMENT '后台第三方请求地址',
  `backEndAddressB` varchar(255) DEFAULT '',
  `backEndPath` varchar(255) DEFAULT NULL COMMENT '后台请求第三方的路径地址,例如: /listUser, 此值必须以/开始',
  `backEndHttpMethod` int(1) DEFAULT NULL COMMENT '后端请求方式,1:get,2:post',
  `backEndTimeout` int(5) DEFAULT NULL COMMENT '设置后台超时时间, connectiontimeout,readtimeout,sockettimeout,最大值30000,单位毫秒',
  `isMock` int(1) DEFAULT NULL COMMENT '是否使用 mock 数据, 1:使用,2:不使用.',
  `mockData` text COMMENT '如果使用了 mock 数据,那么必须要填写 mock 数据值,用户自己输入.',
  `callBackType` int(1) DEFAULT NULL COMMENT '返回信息类型, 1:json',
  `callBackSuccessExample` text COMMENT '返回成功示例',
  `callBackFailExample` text COMMENT '返回失败示例',
  `env` int(1) DEFAULT NULL COMMENT '发布过的环境, 1:线上,2:测试',
  `currentVersion` int(1) DEFAULT NULL COMMENT '是否当前发布版本,1:是,2:不是',
  `pubDescription` varchar(255) DEFAULT NULL COMMENT '发布的描述信息',
  `saveMongoDB` int(1) DEFAULT NULL COMMENT '是否保存 mongodb,1:保存,2:不保存',
  `mongodbURI` varchar(255) DEFAULT NULL COMMENT '是否保存 mongodb的URI',
  `mongodbDBName` varchar(255) DEFAULT NULL COMMENT 'mongodb 数据库名称',
  `mongodbCollectionName` varchar(255) DEFAULT NULL COMMENT 'mongodb collection名称',
  `saveMQ` int(1) DEFAULT NULL COMMENT '是否保存 mq,1:保存,2:不保存',
  `mqType` int(1) DEFAULT NULL COMMENT '1:rabbbitmq,2:kafka',
  `mqAddress` varchar(255) DEFAULT NULL COMMENT 'mq 的地址',
  `mqUserName` varchar(255) DEFAULT NULL COMMENT 'mq 用户名',
  `mqPasswd` varchar(255) DEFAULT NULL COMMENT 'mq 密码',
  `mqTopicName` varchar(255) DEFAULT NULL COMMENT '队列名称',
  `createTime` bigint(13) DEFAULT NULL COMMENT 'api创建时间',
  `weight` int(3) DEFAULT NULL COMMENT '权重 0 - 10',
  `charge` int(3) DEFAULT NULL COMMENT '是否计费 1：是 2：否',
  `uniqueUuid` varchar(10) DEFAULT NULL COMMENT '上游接口的唯一标识',
  `limitStrategyuuid` varchar(64) DEFAULT NULL COMMENT '该用户绑定的限流策略 uuid',
  `interfaceName` varchar(100) DEFAULT NULL COMMENT '上游接口名称',
  `backendProtocolType` int(2) DEFAULT NULL COMMENT '上游协议类型 1:http 2:https',
  `cacheUnit` int(1) DEFAULT NULL COMMENT '三方结果缓存时间单位 1:小时 2:天 3:不限',
  `cacheNo` int(10) DEFAULT NULL COMMENT '三方结果缓存时间',
  `responseTransParam` mediumtext COMMENT '响应字段需要转码的json集合',
  `responseConfigJson` mediumtext COMMENT '转换的原始json',
  `jsonConfig` mediumtext,
  `autoTest` varchar(10) DEFAULT NULL COMMENT '是否允许自动测试,是：Y，否：N',
  `test_description` varchar(255) DEFAULT NULL COMMENT '接口测试注释,有些接口不能测试，就在此字段添加描述信息',
  `limitStrategyTotal` int(11) DEFAULT '-1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `versionId` (`versionId`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 COMMENT='API的总体信息历史版本';

-- ----------------------------
-- Table structure for api_mapping_info
-- ----------------------------
DROP TABLE IF EXISTS `api_mapping_info`;
CREATE TABLE `api_mapping_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `path` varchar(255) NOT NULL,
  `serviceId` varchar(255) DEFAULT NULL,
  `aUrl` varchar(255) NOT NULL,
  `bUrl` varchar(255) DEFAULT NULL,
  `stripPrefix` char(1) DEFAULT '1',
  `retryable` char(1) DEFAULT '1',
  `enable` char(1) DEFAULT '1',
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000;

-- ----------------------------
-- Table structure for api_offline_record
-- ----------------------------
DROP TABLE IF EXISTS `api_offline_record`;
CREATE TABLE `api_offline_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `api_id` int(11) DEFAULT NULL COMMENT 'api的对应id',
  `api_group_id` int(11) DEFAULT NULL COMMENT '分组',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` bigint(20) DEFAULT NULL COMMENT '下线时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 COMMENT='下线历史立即表';

-- ----------------------------
-- Table structure for api_rate_distribute
-- ----------------------------
DROP TABLE IF EXISTS `api_rate_distribute`;
CREATE TABLE `api_rate_distribute` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增 id',
  `apiId` int(11) NOT NULL COMMENT '父API的apiId',
  `uniqueUuid` varchar(10) DEFAULT NULL COMMENT '上游接口的唯一标识',
  `interfaceName` varchar(100) DEFAULT NULL COMMENT '上游接口名称',
  `backEndAddress` varchar(255) DEFAULT NULL COMMENT '后台第三方请求地址',
  `backEndAddressB` varchar(255) DEFAULT '',
  `backEndPath` varchar(255) DEFAULT NULL COMMENT '后台请求第三方的路径地址,例如: /listUser, 此值必须以/开始',
  `backEndHttpMethod` int(1) DEFAULT NULL COMMENT '后端请求方式,1:get,2:post',
  `backEndTimeout` int(5) DEFAULT NULL COMMENT '设置后台超时时间, connectiontimeout,readtimeout,sockettimeout,最大值30000,单位毫秒',
  `callBackType` int(1) DEFAULT '1' COMMENT '返回信息类型, 1:json',
  `status` int(1) DEFAULT NULL COMMENT '状态码,1:代表可用,2代表不可用',
  `weight` int(3) DEFAULT NULL COMMENT '权重 0 - 10',
  `createTime` bigint(13) DEFAULT NULL COMMENT 'api创建时间',
  `updateTime` bigint(13) DEFAULT NULL COMMENT 'api 更新时间',
  `backendProtocolType` int(2) DEFAULT NULL COMMENT '上游协议类型 1:http 2:https',
  `responseTransParam` text COMMENT '响应字段需要转码的json集合',
  `responseConfigJson` text COMMENT '转换的原始json',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 COMMENT='流量分发的子API';

-- ----------------------------
-- Table structure for api_result_etl
-- ----------------------------
DROP TABLE IF EXISTS `api_result_etl`;
CREATE TABLE `api_result_etl` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `apiId` int(11) NOT NULL COMMENT 'API的apiId',
  `disId` int(11) NOT NULL COMMENT 'api_rate_distribute表的id',
  `eKey` varchar(50) DEFAULT NULL COMMENT '抽取的key，可用:表示层级关系，没有:表示单层关系',
  `tKey` varchar(50) DEFAULT NULL COMMENT '转换的key，为空则表示不用转换',
  `tValue` varchar(500) DEFAULT NULL COMMENT '值的对应关系，存进来是一个json，取出后转成map，保留功能',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `createTime` bigint(13) DEFAULT NULL COMMENT '创建时间',
  `updateTime` bigint(13) DEFAULT NULL COMMENT '修改时间',
  `status` int(2) DEFAULT NULL COMMENT '状态码,1:代表可用,2代表不可用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 COMMENT='三方结果的参数抽取与转换';

-- ----------------------------
-- Table structure for api_result_etl_versions
-- ----------------------------
DROP TABLE IF EXISTS `api_result_etl_versions`;
CREATE TABLE `api_result_etl_versions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `versionId` varchar(50) NOT NULL COMMENT 'version id',
  `apiId` int(11) NOT NULL COMMENT 'API的apiId',
  `eKey` varchar(50) DEFAULT NULL COMMENT '抽取的key，可用:表示层级关系，没有:表示单层关系',
  `tKey` varchar(50) DEFAULT NULL COMMENT '转换的key，为空则表示不用转换',
  `tValue` varchar(500) DEFAULT NULL COMMENT '值的对应关系，存进来是一个json，取出后转成map，保留功能',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `status` int(1) DEFAULT NULL COMMENT '状态码,1:代表可用,2代表不可用',
  `createTime` bigint(13) DEFAULT NULL COMMENT '创建时间',
  `updateTime` bigint(13) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 COMMENT='三方结果的参数抽取与转换版本表';

-- ----------------------------
-- Table structure for apiresultsettings
-- ----------------------------
DROP TABLE IF EXISTS `apiresultsettings`;
CREATE TABLE `apiresultsettings` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `apiId` int(11) NOT NULL COMMENT 'apiId',
  `errorCode` int(10) DEFAULT NULL COMMENT '错误码',
  `errorMsg` varchar(255) DEFAULT NULL COMMENT '错误信息',
  `lookupInfo` varchar(255) DEFAULT NULL COMMENT '需要查询的信息',
  `errorDescription` varchar(255) DEFAULT NULL COMMENT '该错误码描述信息',
  `status` int(1) DEFAULT NULL COMMENT '状态码,1可用,2不可用',
  `createTime` bigint(13) DEFAULT NULL COMMENT '创建时间',
  `updateTime` bigint(13) DEFAULT NULL COMMENT '结束时间',
  `disId` int(11) DEFAULT NULL COMMENT '子接口表的id，此接口不走版本',
  PRIMARY KEY (`id`),
  KEY `apiId` (`apiId`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 COMMENT='该 api 返回给客户的最终结果映射表, 返回的结果统一格式如下:\r\n{\r\n    "errorCode": 200,\r\n    "errorMsg": "请求成功",\r\n    "data": {\r\n        "jsonObj": {\r\n            "id": "1",\r\n            "name": "lmw"\r\n        }\r\n    }\r\n}';

-- ----------------------------
-- Table structure for apiResultSettings
-- ----------------------------
DROP TABLE IF EXISTS `apiResultSettings`;
CREATE TABLE `apiResultSettings` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `apiId` int(11) NOT NULL COMMENT 'apiId',
  `errorCode` int(10) DEFAULT NULL COMMENT '错误码',
  `errorMsg` varchar(255) DEFAULT NULL COMMENT '错误信息',
  `lookupInfo` varchar(255) DEFAULT NULL COMMENT '需要查询的信息',
  `errorDescription` varchar(255) DEFAULT NULL COMMENT '该错误码描述信息',
  `status` int(1) DEFAULT NULL COMMENT '状态码,1可用,2不可用',
  `createTime` bigint(13) DEFAULT NULL COMMENT '创建时间',
  `updateTime` bigint(13) DEFAULT NULL COMMENT '结束时间',
  `disId` int(11) DEFAULT NULL COMMENT '子接口表的id，此接口不走版本',
  PRIMARY KEY (`id`),
  KEY `apiId` (`apiId`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 COMMENT='该 api 返回给客户的最终结果映射表, 返回的结果统一格式如下:\r\n{\r\n    "errorCode": 200,\r\n    "errorMsg": "请求成功",\r\n    "data": {\r\n        "jsonObj": {\r\n            "id": "1",\r\n            "name": "lmw"\r\n        }\r\n    }\r\n}';

-- ----------------------------
-- Table structure for apiresultsettings_versions
-- ----------------------------
DROP TABLE IF EXISTS `apiresultsettings_versions`;
CREATE TABLE `apiresultsettings_versions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `apiId` int(11) NOT NULL COMMENT 'apiId',
  `versionId` varchar(17) NOT NULL DEFAULT '' COMMENT '版本号',
  `errorCode` int(10) DEFAULT NULL COMMENT '给客户端返回的状态码',
  `errorMsg` varchar(255) DEFAULT NULL COMMENT '错误码',
  `lookupInfo` varchar(255) DEFAULT NULL COMMENT '需要查询的信息',
  `errorDescription` varchar(255) DEFAULT NULL COMMENT '该错误码描述信息',
  `createTime` bigint(13) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `versionId` (`versionId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1000 COMMENT='该 api 返回给客户的最终结果映射表, 返回的结果统一格式如下:  历史版本\r\n{\r\n    "errorCode": 200,\r\n    "errorMsg": "请求成功",\r\n    "data": {\r\n        "jsonObj": {\r\n            "id": "1",\r\n            "name": "lmw"\r\n        }\r\n    }\r\n}';

-- ----------------------------
-- Table structure for apiResultSettings_versions
-- ----------------------------
DROP TABLE IF EXISTS `apiResultSettings_versions`;
CREATE TABLE `apiResultSettings_versions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `apiId` int(11) NOT NULL COMMENT 'apiId',
  `versionId` varchar(17) NOT NULL DEFAULT '' COMMENT '版本号',
  `errorCode` int(10) DEFAULT NULL COMMENT '给客户端返回的状态码',
  `errorMsg` varchar(255) DEFAULT NULL COMMENT '错误码',
  `lookupInfo` varchar(255) DEFAULT NULL COMMENT '需要查询的信息',
  `errorDescription` varchar(255) DEFAULT NULL COMMENT '该错误码描述信息',
  `createTime` bigint(13) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `versionId` (`versionId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1000 COMMENT='该 api 返回给客户的最终结果映射表, 返回的结果统一格式如下:  历史版本\r\n{\r\n    "errorCode": 200,\r\n    "errorMsg": "请求成功",\r\n    "data": {\r\n        "jsonObj": {\r\n            "id": "1",\r\n            "name": "lmw"\r\n        }\r\n    }\r\n}';

-- ----------------------------
-- Table structure for app_certification
-- ----------------------------
DROP TABLE IF EXISTS `app_certification`;
CREATE TABLE `app_certification` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `appName` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `appKey` varchar(10) DEFAULT '',
  `appSecret` varchar(32) DEFAULT NULL,
  `description` text,
  `status` int(1) DEFAULT '1' COMMENT '状态码, 1:状态可用, 2:状态不可用.',
  `old` int(2) DEFAULT NULL COMMENT '是否是计费系统的appkey 1：是 2：否',
  `createTime` bigint(13) DEFAULT NULL,
  `updateTime` bigint(13) DEFAULT NULL,
  `limitStrategyuuid` varchar(64) DEFAULT NULL COMMENT '该用户绑定的限流策略 id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 COMMENT='App 授权应用信息';

-- ----------------------------
-- Table structure for auth_different_stock
-- ----------------------------
DROP TABLE IF EXISTS `auth_different_stock`;
CREATE TABLE `auth_different_stock` (
  `id` int(13) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_key` varchar(64) NOT NULL COMMENT '客户appKey',
  `api_id` int(11) NOT NULL COMMENT '接口id',
  `stock_value` int(11) DEFAULT NULL COMMENT '余量差值',
  `create_time` bigint(13) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 COMMENT='合约余量比较差值(flink和redis)';

-- ----------------------------
-- Table structure for auth_stock_history
-- ----------------------------
DROP TABLE IF EXISTS `auth_stock_history`;
CREATE TABLE `auth_stock_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `api_id` int(11) DEFAULT NULL COMMENT '接口id',
  `app_key` varchar(256) DEFAULT NULL COMMENT '客户',
  `stock` int(11) DEFAULT NULL COMMENT '余量',
  `create_time` bigint(13) DEFAULT NULL COMMENT '日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 COMMENT='接口调用记录';

-- ----------------------------
-- Table structure for auto_test_result
-- ----------------------------
DROP TABLE IF EXISTS `auto_test_result`;
CREATE TABLE `auto_test_result` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `api_id` int(11) DEFAULT NULL,
  `api_group_id` int(11) DEFAULT NULL,
  `test_result` longtext,
  `test_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `api_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000;

-- ----------------------------
-- Table structure for backend_distribute_params
-- ----------------------------
DROP TABLE IF EXISTS `backend_distribute_params`;
CREATE TABLE `backend_distribute_params` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `apiId` int(11) NOT NULL COMMENT '父API的apiId',
  `disId` int(11) NOT NULL COMMENT 'api_rate_distribute表的id',
  `requestParamsId` int(11) DEFAULT NULL,
  `paramsType` int(1) DEFAULT NULL COMMENT '1:代表变量,需要动态取值, 2:代表常量, 3:json, 4:xml',
  `paramName` varchar(255) DEFAULT NULL COMMENT '参数名称',
  `paramValue` text COMMENT '参数值,常量才会有值',
  `paramsLocation` int(1) DEFAULT NULL COMMENT '1:代表在 head 里面, 2: 代表在 query 里面(get 方式才有 query), 3:代表 body 里面(post 方式才有 body)',
  `paramDescription` varchar(255) DEFAULT NULL COMMENT '参数位置',
  `status` int(1) DEFAULT NULL COMMENT '状态码,1:代表可用,2代表不可用',
  `createTime` bigint(13) DEFAULT NULL COMMENT '创建时间',
  `updateTime` bigint(13) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 COMMENT='做流量分发的子接口后端参数';

-- ----------------------------
-- Table structure for backend_request_params
-- ----------------------------
DROP TABLE IF EXISTS `backend_request_params`;
CREATE TABLE `backend_request_params` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `apiId` int(11) NOT NULL COMMENT 'api 的 id',
  `requestParamsId` int(11) DEFAULT NULL,
  `paramsType` int(1) DEFAULT NULL COMMENT '1:代表变量,需要动态取值, 2:代表常量, 3:json, 4:xml',
  `paramName` varchar(255) DEFAULT NULL COMMENT '参数名',
  `paramValue` text COMMENT '参数值',
  `paramsLocation` int(1) DEFAULT NULL COMMENT '1:代表在 head 里面, 2: 代表在 query 里面(get 方式才有 query), 3:代表 body 里面(post 方式才有 body)',
  `paramDescription` varchar(255) DEFAULT NULL COMMENT '参数位置',
  `status` int(1) DEFAULT '1' COMMENT '1:代表可用, 2:代表不可用',
  `createTime` bigint(13) DEFAULT NULL COMMENT '创建时间',
  `updateTime` bigint(13) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `apiId` (`apiId`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 COMMENT='请求第三方配置的一些参数';

-- ----------------------------
-- Table structure for backend_request_params_copy
-- ----------------------------
DROP TABLE IF EXISTS `backend_request_params_copy`;
CREATE TABLE `backend_request_params_copy` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `apiId` int(11) NOT NULL COMMENT 'api 的 id',
  `requestParamsId` int(11) DEFAULT NULL,
  `paramsType` int(1) DEFAULT NULL COMMENT '1:代表变量,需要动态取值, 2:代表常量, 3:json, 4:xml',
  `paramName` varchar(255) DEFAULT NULL COMMENT '参数名',
  `paramValue` text COMMENT '参数值',
  `paramsLocation` int(1) DEFAULT NULL COMMENT '1:代表在 head 里面, 2: 代表在 query 里面(get 方式才有 query), 3:代表 body 里面(post 方式才有 body)',
  `paramDescription` varchar(255) DEFAULT NULL COMMENT '参数位置',
  `status` int(1) DEFAULT '1' COMMENT '1:代表可用, 2:代表不可用',
  `createTime` bigint(13) DEFAULT NULL COMMENT '创建时间',
  `updateTime` bigint(13) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `apiId` (`apiId`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 COMMENT='请求第三方配置的一些参数';

-- ----------------------------
-- Table structure for backend_request_params_versions
-- ----------------------------
DROP TABLE IF EXISTS `backend_request_params_versions`;
CREATE TABLE `backend_request_params_versions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `versionId` varchar(17) NOT NULL DEFAULT '' COMMENT '版本号',
  `backendParamsId` int(11) DEFAULT NULL COMMENT '后端参数id，对应backend_request_params表中id字段',
  `requestParamsId` int(11) DEFAULT NULL,
  `paramsType` int(1) DEFAULT NULL COMMENT '1:代表变量,需要动态取值, 2:代表常量, 3:json, 4:xml',
  `paramName` varchar(255) DEFAULT NULL COMMENT '参数名称',
  `paramValue` text COMMENT '参数值',
  `paramsLocation` int(1) DEFAULT NULL COMMENT '1:代表在 head 里面, 2: 代表在 query 里面(get 方式才有 query), 3:代表 body 里面(post 方式才有 body)',
  `paramDescription` varchar(255) DEFAULT NULL COMMENT '参数位置',
  `createTime` bigint(13) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `versionId` (`versionId`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 COMMENT='请求第三方配置的一些参数历史版本';

-- ----------------------------
-- Table structure for billing_rules
-- ----------------------------
DROP TABLE IF EXISTS `billing_rules`;
CREATE TABLE `billing_rules` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增 id',
  `uuid` varchar(64) DEFAULT NULL COMMENT '规则唯一id',
  `name` varchar(255) DEFAULT NULL COMMENT '规则名称',
  `billing_type` int(1) DEFAULT NULL COMMENT '计费分类：1-时间计费 2-按条计费',
  `billing_cycle` int(1) DEFAULT NULL COMMENT '计费周期(1-按年，2-季度，3-月,-1不限时)',
  `billing_cycle_limit` bigint(11) DEFAULT NULL COMMENT '时间计费时生效，限定周期内的最大调用量，-1表示不限量',
  `billing_mode` int(1) DEFAULT '1' COMMENT '计费模式(默认1查询计费，2查得计费)',
  `create_time` bigint(13) DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint(13) DEFAULT NULL COMMENT '更新时间',
  `status` int(1) DEFAULT NULL COMMENT '状态码,1启用,0禁用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 COMMENT='计费规则表';

-- ----------------------------
-- Table structure for call_back_param
-- ----------------------------
DROP TABLE IF EXISTS `call_back_param`;
CREATE TABLE `call_back_param` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `api_id` int(11) NOT NULL,
  `param_name` varchar(255) NOT NULL,
  `param_type` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `position` char(1) DEFAULT NULL,
  `parent_id` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000;

-- ----------------------------
-- Table structure for company_apps
-- ----------------------------
DROP TABLE IF EXISTS `company_apps`;
CREATE TABLE `company_apps` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `app_key` varchar(255) DEFAULT NULL COMMENT 'appKey机构标识',
  `app_name` varchar(255) DEFAULT NULL COMMENT 'app名称',
  `create_time` bigint(13) DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint(13) DEFAULT NULL,
  `status_flag` int(1) DEFAULT '1' COMMENT '是否启用,1启用,0禁用',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `balance` decimal(10,2) NOT NULL COMMENT '余额',
  `strategy_uuid` varchar(255) DEFAULT NULL COMMENT '客户级别的计费方式',
  `stock` bigint(13) DEFAULT NULL COMMENT '调用余量',
  `price` decimal(10,2) DEFAULT NULL COMMENT '价格,按条计费时为单价，包时计费为总价格',
  `public_key` varchar(512) NOT NULL COMMENT '公钥',
  `private_key` varchar(1024) NOT NULL COMMENT '私钥',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 COMMENT='客户app表';

-- ----------------------------
-- Table structure for company_apps_auth
-- ----------------------------
DROP TABLE IF EXISTS `company_apps_auth`;
CREATE TABLE `company_apps_auth` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增 id',
  `api_id` int(11) NOT NULL COMMENT '接口id',
  `app_key` varchar(255) NOT NULL COMMENT '客户标识',
  `start_time` bigint(13) NOT NULL COMMENT '接口调用有效起始时间',
  `end_time` bigint(13) NOT NULL COMMENT '接口调用终止时间',
  `create_time` bigint(13) NOT NULL COMMENT '创建时间',
  `update_time` bigint(13) NOT NULL COMMENT '更新时间',
  `status` int(1) NOT NULL DEFAULT '1' COMMENT '状态码,1启用,0禁用',
  `strategy_uuid` varchar(255) DEFAULT NULL COMMENT '计费策略uuid',
  `stock` bigint(13) DEFAULT NULL COMMENT '调用余量',
  `price` decimal(10,2) DEFAULT NULL COMMENT '价格,按条计费时为单价，包时计费为总价格',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 COMMENT='用户接口权限表';

-- ----------------------------
-- Table structure for company_apps_auth_version
-- ----------------------------
DROP TABLE IF EXISTS `company_apps_auth_version`;
CREATE TABLE `company_apps_auth_version` (
  `id` bigint(13) NOT NULL AUTO_INCREMENT,
  `api_id` int(11) DEFAULT NULL,
  `app_key` varchar(255) DEFAULT NULL,
  `start_time` bigint(13) DEFAULT NULL,
  `end_time` bigint(13) DEFAULT NULL,
  `create_time` bigint(13) DEFAULT NULL,
  `update_time` bigint(13) DEFAULT NULL,
  `strategy_uuid` varchar(255) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000;

-- ----------------------------
-- Table structure for company_apps_recharge
-- ----------------------------
DROP TABLE IF EXISTS `company_apps_recharge`;
CREATE TABLE `company_apps_recharge` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_key` varchar(255) DEFAULT NULL COMMENT 'appKey机构标识',
  `app_name` varchar(255) DEFAULT NULL COMMENT 'app名称',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '金额',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '充值备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000;

-- ----------------------------
-- Table structure for company_apps_version
-- ----------------------------
DROP TABLE IF EXISTS `company_apps_version`;
CREATE TABLE `company_apps_version` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `app_key` varchar(255) DEFAULT NULL,
  `strategy_uuid` varchar(255) DEFAULT NULL COMMENT '计费规则',
  `create_time` bigint(13) DEFAULT NULL,
  `update_time` bigint(13) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL COMMENT '单价',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000;

-- ----------------------------
-- Table structure for company_balance_history
-- ----------------------------
DROP TABLE IF EXISTS `company_balance_history`;
CREATE TABLE `company_balance_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_key` varchar(256) DEFAULT NULL COMMENT '客户',
  `balance` decimal(10,2) DEFAULT NULL COMMENT '余额',
  `stock` int(20) DEFAULT NULL COMMENT '余量',
  `create_time` bigint(13) DEFAULT NULL COMMENT '日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 COMMENT='客户余额余量记录';

-- ----------------------------
-- Table structure for company_bill_day
-- ----------------------------
DROP TABLE IF EXISTS `company_bill_day`;
CREATE TABLE `company_bill_day` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_key` varchar(255) NOT NULL COMMENT '客户标识',
  `called` bigint(20) DEFAULT NULL COMMENT '调用量',
  `stock` bigint(20) DEFAULT NULL COMMENT '剩余量',
  `amount` decimal(10,6) DEFAULT NULL COMMENT '消费额',
  `balance` decimal(10,6) DEFAULT NULL COMMENT '余额',
  `create_time` bigint(13) NOT NULL COMMENT '日期-天',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 COMMENT='客户日账单表';

-- ----------------------------
-- Table structure for company_bill_day_datail
-- ----------------------------
DROP TABLE IF EXISTS `company_bill_day_datail`;
CREATE TABLE `company_bill_day_datail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_key` varchar(255) DEFAULT NULL COMMENT '客户标识',
  `api_id` int(11) DEFAULT NULL COMMENT '接口id',
  `billing_rules_uuid` varchar(64) DEFAULT NULL COMMENT '规则uuid',
  `price` decimal(10,2) DEFAULT NULL COMMENT '单价',
  `count` bigint(20) DEFAULT NULL COMMENT '调用量',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '总额，时间计费规则不计算总额',
  `create_time` bigint(13) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 COMMENT='客户调用明细表';

-- ----------------------------
-- Table structure for company_bill_month
-- ----------------------------
DROP TABLE IF EXISTS `company_bill_month`;
CREATE TABLE `company_bill_month` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_key` varchar(255) DEFAULT NULL COMMENT '客户标识',
  `called` bigint(20) DEFAULT NULL COMMENT '调用量',
  `stock` bigint(20) DEFAULT NULL COMMENT '剩余量',
  `amount` decimal(10,6) DEFAULT NULL COMMENT '消费额',
  `balance` decimal(10,6) DEFAULT NULL COMMENT '余额',
  `create_time` bigint(13) DEFAULT NULL COMMENT '日期-月份',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 COMMENT='客户日账单表';

-- ----------------------------
-- Table structure for company_bill_year
-- ----------------------------
DROP TABLE IF EXISTS `company_bill_year`;
CREATE TABLE `company_bill_year` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_key` varchar(255) NOT NULL COMMENT '客户标识',
  `called` bigint(20) DEFAULT NULL COMMENT '调用量',
  `stock` bigint(20) DEFAULT NULL COMMENT '剩余量',
  `amount` decimal(10,6) DEFAULT NULL COMMENT '消费额',
  `balance` decimal(10,6) DEFAULT NULL COMMENT '余额',
  `create_time` bigint(13) NOT NULL COMMENT '日期-月份',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 COMMENT='客户日账单表';

-- ----------------------------
-- Table structure for company_white_ip
-- ----------------------------
DROP TABLE IF EXISTS `company_white_ip`;
CREATE TABLE `company_white_ip` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` date DEFAULT NULL COMMENT '创建时间',
  `update_time` date DEFAULT NULL,
  `app_key_id` bigint(11) DEFAULT NULL,
  `ip_address` varchar(20) DEFAULT NULL COMMENT 'ip地址',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `status_flag` int(11) DEFAULT '1' COMMENT '是否启用,1启用,0禁用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 COMMENT='公司IP白名单';

-- ----------------------------
-- Table structure for current_limit_strategy
-- ----------------------------
DROP TABLE IF EXISTS `current_limit_strategy`;
CREATE TABLE `current_limit_strategy` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(64) DEFAULT NULL COMMENT '策略自动生成 uuid',
  `name` varchar(255) DEFAULT NULL COMMENT '策略名称',
  `unit` int(1) DEFAULT NULL COMMENT '单位, 1:代表分钟,2:代表小时,3:代表天',
  `no` int(11) DEFAULT NULL COMMENT '限制总次数',
  `status` int(1) DEFAULT '1' COMMENT '状态码,1:代表可用, 2:代表不可用',
  `createTime` bigint(13) DEFAULT NULL COMMENT '创建时间',
  `updateTime` bigint(13) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uuid` (`uuid`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 COMMENT='限流策略';

-- ----------------------------
-- Table structure for customer_different_balance
-- ----------------------------
DROP TABLE IF EXISTS `customer_different_balance`;
CREATE TABLE `customer_different_balance` (
  `id` bigint(13) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_key` varchar(64) NOT NULL COMMENT '客户账号',
  `balance_value` decimal(10,2) DEFAULT NULL COMMENT '余额差值',
  `stock_value` int(11) DEFAULT NULL COMMENT '余量差值',
  `create_time` bigint(13) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  COMMENT='客户余额差值(redis和flink)';

-- ----------------------------
-- Table structure for datasource_bill_day
-- ----------------------------
DROP TABLE IF EXISTS `datasource_bill_day`;
CREATE TABLE `datasource_bill_day` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_name` varchar(255) DEFAULT NULL COMMENT '数据源标识',
  `called` bigint(20) DEFAULT NULL COMMENT '调用量',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '消费额',
  `create_time` bigint(13) DEFAULT NULL COMMENT '日期-天',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 COMMENT='数据源日账单表';

-- ----------------------------
-- Table structure for datasource_bill_day_detail
-- ----------------------------
DROP TABLE IF EXISTS `datasource_bill_day_detail`;
CREATE TABLE `datasource_bill_day_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_name` varchar(255) DEFAULT NULL COMMENT '数据源标识',
  `api_id` int(11) DEFAULT NULL COMMENT '接口id',
  `billing_rules_uuid` varchar(64) DEFAULT NULL COMMENT '规则uuid',
  `price` decimal(10,2) DEFAULT NULL COMMENT '单价',
  `count` bigint(20) DEFAULT NULL COMMENT '调用量',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '总额，时间计费规则不计算总额',
  `create_time` bigint(13) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 COMMENT='数据源调用明细表';

-- ----------------------------
-- Table structure for datasource_bill_month
-- ----------------------------
DROP TABLE IF EXISTS `datasource_bill_month`;
CREATE TABLE `datasource_bill_month` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_name` varchar(255) DEFAULT NULL COMMENT '数据源标识',
  `called` bigint(20) DEFAULT NULL COMMENT '调用量',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '消费额',
  `create_time` bigint(13) DEFAULT NULL COMMENT '日期-月份',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 COMMENT='数据源月账单表';

-- ----------------------------
-- Table structure for datasource_bill_year
-- ----------------------------
DROP TABLE IF EXISTS `datasource_bill_year`;
CREATE TABLE `datasource_bill_year` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_name` varchar(255) DEFAULT NULL COMMENT '数据源标识',
  `called` bigint(20) DEFAULT NULL COMMENT '调用量',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '消费额',
  `create_time` bigint(13) DEFAULT NULL COMMENT '日期-月份',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 COMMENT='数据源年账单表';

-- ----------------------------
-- Table structure for datasource_charge
-- ----------------------------
DROP TABLE IF EXISTS `datasource_charge`;
CREATE TABLE `datasource_charge` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `group_id` int(11) DEFAULT NULL COMMENT '数据源ID',
  `api_id` int(11) DEFAULT NULL COMMENT '接口ID',
  `billing_rules_uuid` varchar(32) DEFAULT NULL COMMENT '计费规则ID',
  `price` decimal(10,4) DEFAULT NULL COMMENT '单价',
  `start_time` bigint(13) DEFAULT NULL COMMENT '创建时间',
  `end_time` bigint(13) DEFAULT NULL COMMENT '更新时间',
  `status` int(1) NOT NULL DEFAULT '0' COMMENT '状态,0:可用, 1:不可用',
  `stock` int(11) DEFAULT '0' COMMENT '总量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 COMMENT='数据源计费管理';

-- ----------------------------
-- Table structure for datasource_info
-- ----------------------------
DROP TABLE IF EXISTS `datasource_info`;
CREATE TABLE `datasource_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) DEFAULT NULL COMMENT '数据源名称',
  `doc_id` int(11) DEFAULT NULL COMMENT '数据源文档ID',
  `tel` varchar(11) DEFAULT NULL COMMENT '电话',
  `mail` varchar(64) DEFAULT NULL COMMENT '邮箱',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  COMMENT='数据源信息';

-- ----------------------------
-- Table structure for dictionary
-- ----------------------------
DROP TABLE IF EXISTS `dictionary`;
CREATE TABLE `dictionary` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `typeId` int(11) NOT NULL COMMENT '所属类别Id',
  `dictName` varchar(110) COLLATE utf8_bin DEFAULT NULL,
  `dictValue` varchar(110) COLLATE utf8_bin DEFAULT NULL,
  `status` int(2) DEFAULT NULL COMMENT '状态',
  `createTime` bigint(13) DEFAULT NULL,
  `updateTime` bigint(13) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 COLLATE=utf8_bin COMMENT='数据字典表';

-- ----------------------------
-- Table structure for dictionary_item
-- ----------------------------
DROP TABLE IF EXISTS `dictionary_item`;
CREATE TABLE `dictionary_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dictionaryId` int(11) NOT NULL COMMENT 'dictionary表中的id',
  `dictItemName` varchar(110) COLLATE utf8_bin DEFAULT NULL,
  `dictItemValue` varchar(110) COLLATE utf8_bin DEFAULT NULL,
  `status` int(2) DEFAULT NULL,
  `createTime` bigint(13) DEFAULT NULL,
  `updateTime` bigint(13) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for dictionary_type
-- ----------------------------
DROP TABLE IF EXISTS `dictionary_type`;
CREATE TABLE `dictionary_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(255) DEFAULT NULL,
  `status` int(2) NOT NULL COMMENT '状态',
  `createTime` bigint(13) NOT NULL,
  `updateTime` bigint(13) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000;

-- ----------------------------
-- Table structure for instance
-- ----------------------------
DROP TABLE IF EXISTS `instance`;
CREATE TABLE `instance` (
  `instance_id` varchar(255) NOT NULL,
  `app_name` varchar(255) DEFAULT NULL,
  `health_check_url` varchar(255) DEFAULT NULL,
  `host_name` varchar(255) DEFAULT NULL,
  `port` int(11) NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`instance_id`)
) ENGINE=InnoDB ;

-- ----------------------------
-- Table structure for rate_limit_appkey
-- ----------------------------
DROP TABLE IF EXISTS `rate_limit_appkey`;
CREATE TABLE `rate_limit_appkey` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `appKey` varchar(30) NOT NULL COMMENT 'appKey',
  `total` int(11) DEFAULT NULL COMMENT 'appKey总量',
  `strategy` varchar(64) DEFAULT NULL COMMENT '对应策略表UUID',
  PRIMARY KEY (`appKey`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 COMMENT='appKey限流';

-- ----------------------------
-- Table structure for rate_limit_appkey_api
-- ----------------------------
DROP TABLE IF EXISTS `rate_limit_appkey_api`;
CREATE TABLE `rate_limit_appkey_api` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `appKey` varchar(30) NOT NULL COMMENT 'appKey',
  `apiId` int(11) NOT NULL COMMENT 'apiId',
  `total` int(11) DEFAULT NULL COMMENT 'appKey总量',
  `strategy` varchar(64) DEFAULT NULL COMMENT '对应策略表UUID',
  PRIMARY KEY (`appKey`,`apiId`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 COMMENT='appKey-api限流';

-- ----------------------------
-- Table structure for redis_data
-- ----------------------------
DROP TABLE IF EXISTS `redis_data`;
CREATE TABLE `redis_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `redis_key` text NOT NULL,
  `redis_value` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000;

-- ----------------------------
-- Table structure for request_params
-- ----------------------------
DROP TABLE IF EXISTS `request_params`;
CREATE TABLE `request_params` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `apiId` int(11) NOT NULL COMMENT 'api 的 id',
  `paramName` varchar(255) DEFAULT NULL COMMENT '参数名称',
  `paramsType` int(1) DEFAULT NULL COMMENT '参数类型, 1:代表String,2:int,3:long,4:float,5:double,6:boolean',
  `paramsLocation` int(1) DEFAULT NULL COMMENT '1:代表在 head 里面, 2: 代表在 query 里面(get 方式才有 query), 3:代表 body 里面(post 方式才有 body)',
  `paramsMust` int(1) DEFAULT '1' COMMENT '该参数是否必填,1:必填,2非必填',
  `paramsDefaultValue` varchar(255) DEFAULT NULL COMMENT '该参数的默认值',
  `paramsExample` varchar(255) DEFAULT NULL COMMENT '参数示例',
  `paramsDescription` varchar(255) DEFAULT NULL COMMENT '参数描述信息',
  `minLength` int(3) DEFAULT NULL COMMENT '最小长度',
  `maxLength` int(3) DEFAULT NULL COMMENT '最大长度',
  `regularExpress` varchar(255) DEFAULT NULL COMMENT '正则表达式',
  `status` int(1) DEFAULT '1' COMMENT '状态,1:可用, 2:不可用',
  `createTime` bigint(13) DEFAULT NULL COMMENT '创建时间',
  `updateTime` bigint(13) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `apiId` (`apiId`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 COMMENT='客户请求我们的请求参数';

-- ----------------------------
-- Table structure for request_params_versions
-- ----------------------------
DROP TABLE IF EXISTS `request_params_versions`;
CREATE TABLE `request_params_versions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `versionId` varchar(17) NOT NULL DEFAULT '' COMMENT 'api 的 id',
  `requestParamsId` int(11) unsigned zerofill NOT NULL,
  `paramName` varchar(255) DEFAULT NULL COMMENT '参数名称',
  `paramsType` int(1) DEFAULT NULL COMMENT '参数类型, 1:代表String,2:int,3:long,4:float,5:double,6:boolean',
  `paramsLocation` int(1) DEFAULT NULL COMMENT '1:代表在 head 里面, 2: 代表在 query 里面(get 方式才有 query), 3:代表 body 里面(post 方式才有 body)',
  `paramsMust` int(1) DEFAULT NULL COMMENT '该参数是否必填,1:必填,2:非必填',
  `paramsDefaultValue` varchar(255) DEFAULT NULL COMMENT '该参数的默认值',
  `paramsExample` varchar(255) DEFAULT NULL COMMENT '参数示例',
  `paramsDescription` varchar(255) DEFAULT NULL COMMENT '参数描述信息',
  `minLength` int(3) DEFAULT NULL COMMENT '最小长度',
  `maxLength` int(3) DEFAULT NULL COMMENT '最大长度',
  `regularExpress` varchar(255) DEFAULT NULL COMMENT '正则表达式',
  `createTime` bigint(13) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `versionId` (`versionId`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 COMMENT='客户请求我们的请求参数历史版本';

-- ----------------------------
-- Table structure for request_params_versions_latest
-- ----------------------------
DROP TABLE IF EXISTS `request_params_versions_latest`;
CREATE TABLE `request_params_versions_latest` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `versionId` varchar(17) NOT NULL DEFAULT '' COMMENT 'api 的 id',
  `apiId` int(11) NOT NULL,
  `requestParamsId` int(11) unsigned zerofill NOT NULL,
  `paramName` varchar(255) DEFAULT NULL COMMENT '参数名称',
  `paramsType` int(1) DEFAULT NULL COMMENT '参数类型, 1:代表String,2:int,3:long,4:float,5:double,6:boolean',
  `paramsLocation` int(1) DEFAULT NULL COMMENT '1:代表在 head 里面, 2: 代表在 query 里面(get 方式才有 query), 3:代表 body 里面(post 方式才有 body)',
  `paramsMust` int(1) DEFAULT NULL COMMENT '该参数是否必填,1:必填,2:非必填',
  `paramsDefaultValue` varchar(255) DEFAULT NULL COMMENT '该参数的默认值',
  `paramsExample` varchar(255) DEFAULT NULL COMMENT '参数示例',
  `paramsDescription` varchar(255) DEFAULT NULL COMMENT '参数描述信息',
  `minLength` int(3) DEFAULT NULL COMMENT '最小长度',
  `maxLength` int(3) DEFAULT NULL COMMENT '最大长度',
  `regularExpress` varchar(255) DEFAULT NULL COMMENT '正则表达式',
  `createTime` bigint(13) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `versionId` (`versionId`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 COMMENT='客户请求我们的请求参数历史版本';

-- ----------------------------
-- Table structure for service_router
-- ----------------------------
DROP TABLE IF EXISTS `service_router`;
CREATE TABLE `service_router` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `path` varchar(255) NOT NULL,
  `serviceId` varchar(255) DEFAULT NULL,
  `url` varchar(255) NOT NULL,
  `stripPrefix` int(1) DEFAULT '1',
  `retryable` int(1) DEFAULT '1',
  `enabled` int(1) DEFAULT '1',
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000;

-- ----------------------------
-- Table structure for system_error_code
-- ----------------------------
DROP TABLE IF EXISTS `system_error_code`;
CREATE TABLE `system_error_code` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `errorCode` int(10) DEFAULT NULL COMMENT '错误码',
  `errorMsg` varchar(255) DEFAULT NULL COMMENT '错误信息',
  `errorDescription` varchar(255) DEFAULT NULL COMMENT '该错误码描述信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 COMMENT='错误码定义';

