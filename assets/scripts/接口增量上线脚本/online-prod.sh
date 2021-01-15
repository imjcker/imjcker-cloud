#!/usr/bin/env bash
##########################################################################################
#目前还有前后端参数不一致问题还没有解决。前后端参数一致的话，可以直接使用此脚本生成的sql进行上线。#
##########################################################################################

host=172.32.3.233
port=3306
user=root
passwd=!sql!sql
dbName=gateway_system


if [ $# -le 0 ] ; then
  echo "请输入至少一个api的id"
  exit 0
fi


for arg in "$@" 
do

api_id=$arg

sql1="SELECT
	apiName,
	IF(apiGroupId is null or apiGroupId = '','empty',apiGroupId),
	httpPath,
	httpMethod,
	apiDescription,
	backEndAddress,
	backEndPath,
	backEndHttpMethod,
	backEndTimeout,
	isMock,
	IF(mockData is null or mockData = '',null,mockData),
	callBackType,
	IF(publishProductEnvStatus is null or publishProductEnvStatus = '',null,publishProductEnvStatus),
	IF(productEnvVersion is null or productEnvVersion,null,productEnvVersion),
	publishTestEnvStatus,
	IF(testEnvVersion is null or testEnvVersion = '',null,testEnvVersion),
	saveMongoDB,
	IF(mongodbURI is null or mongodbURI = '',null,mongodbURI),
	IF(mongodbDBName is null or mongodbDBName = '',null,mongodbDBName),
	IF(mongodbCollectionName is null or mongodbCollectionName = '',null,mongodbCollectionName),
	saveMQ,
	IF(mqType is null or mqType = '','empty',mqType),
	interfaceName,
	IF(mqAddress is null or mqAddress = '',null,mqAddress),
	uniqueUuid,
	IF(mqUserName is null or mqUserName = '',null,mqUserName),
	IF(mqPasswd is null or mqPasswd = '',null,mqPasswd),
	IF(mqTopicName is null or mqTopicName = '',null,mqTopicName),
	status,
	createTime,
	updateTime,
	weight,
	IF(limitStrategyuuid is null or limitStrategyuuid = '','empty',limitStrategyuuid),
	charge,
	backendProtocolType,
	IF(cacheUnit is null or cacheUnit='','empty',cacheUnit),
	IF(cacheNo is null or cacheNo = '','empty',cacheNo)
FROM
	api_info
where 
	id=$api_id;"

api_insert="INSERT INTO api_info(apiName,apiGroupId,httpPath,httpMethod,apiDescription,backEndAddress,backEndPath,backEndHttpMethod,backEndTimeout,isMock,mockData,callBackType,callBackSuccessExample,callBackFailExample,publishProductEnvStatus,productEnvVersion,publishTestEnvStatus,testEnvVersion,saveMongoDB,mongodbURI,mongodbDBName,mongodbCollectionName,saveMQ,mqType,interfaceName,mqAddress,uniqueUuid,mqUserName,mqPasswd,mqTopicName,STATUS,createTime,updateTime,weight,limitStrategyuuid,charge,backendProtocolType,cacheUnit,cacheNo,responseTransParam,responseConfigJson,jsonConfig) VALUES ('@%1','@%2','@%3','@%4','@%5','@%6','@%7','@%8','@%9','@%10','@%11','@%12','@%38','@%39','@%13','@%14','@%15','@%16','@%17','@%18','@%19','@%20','@%21','@%22','@%23','@%24','@%25','@%26','@%27','@%28','@%29','@%30','@%31','@%32','@%33','@%34','@%35','@%36','@%37','@%40','@%41','@%42');"


MYSQL="mysql -h${host} -u${user} -p${passwd} -P${port} -D${dbName}"

echo "------------api_info begin------------"

mysql -h${host} -u${user} -p${passwd} -P${port} -D${dbName} -NBe"set names utf8;$sql1" | while read line; do
  api_info=`echo $line | sed 's/[ ]/\|/g'`
  i=1
  while ((1))
  do
    split=`echo $api_info | cut -d \| -f $i`
    if [ "$split" != "" ] ; then
       api_insert=${api_insert/@%$i/$split}
       (( i++ ))
    else
       echo "字段总数" + $i
       break
    fi
  done
    api_insert=`echo $api_insert | sed "s/'NULL'/''/g"`
    api_insert=`echo $api_insert | sed "s/'empty'/NULL/g"`
  echo $api_insert > ./text.txt
done

sql2="select callBackSuccessExample from api_info where id = $api_id;"
success="$($MYSQL -NBe"set names utf8;$sql2")"
sql3="select callBackFailExample from api_info where id =$api_id ;"
fail="$($MYSQL -NBe"set names utf8;$sql3")"
sql4="select responseConfigJson from api_info where id = $api_id;"
config="$($MYSQL -NBe"set names utf8;$sql4")"
sql5="select responseTransParam from api_info where id =$api_id ;"
trans="$($MYSQL -NBe"set names utf8;$sql5")"
sql_1="select jsonConfig from api_info where id =$api_id ;"
json_Config="$($MYSQL -NBe"set names utf8;$sql_1")"
sql6="select apiName from api_info where id = $api_id;"
api_name="$($MYSQL -NBe"set names utf8;$sql6")"

api_insert=""

while read line ; do
  i1=${line/@%38/$success}
  i2=${i1/@%39/$fail}
  i3=${i2/@%40/$trans}
  i4=${i3/@%41/$config}
  api_insert=${i4/@%42/$json_Config}
done < ./text.txt

echo $api_insert >> ./insert_api.sql

echo "-----------api_info finish-----------------"


echo "select id from api_info where apiName='$api_name' into @result;" >> ./insert_api.sql

echo "-----------api_group_relation begin--------"

sql7="select apiId,groupId,path,createTime,fullPathName from api_group_relation where apiId=$api_id;"

mysql -h${host} -u${user} -p${passwd} -P${port} -D${dbName} -NBe"set names utf8;$sql7" | while read line; do

  result=`echo $line | sed 's/[ ]/\|/g'`
  i1=`echo $result | cut -d \| -f 1`
  i2=`echo $result | cut -d \| -f 2`
  i3=`echo $result | cut -d \| -f 3`
  i3_new=`echo $i3 | sed 's/[0-9]*$//'`
  i4=`echo $result | cut -d \| -f 4`
  i5=`echo $result | cut -d \| -f 5`
  echo "INSERT INTO api_group_relation (apiId, groupId, path, createTime, fullPathName) VALUES (@result,'$i2',CONCAT('$i3_new',@result),'$i4','$i5');" >> ./insert_api.sql
done


echo "-----------api_group_relation finish-----"


echo "-----------request_params begin--------"

sql8="SELECT
	apiId,
	paramName,
	paramsType,
	paramsLocation,
	paramsMust,
	if(paramsDefaultValue is null or paramsDefaultValue = '',null,paramsDefaultValue),
	if(paramsExample is null or paramsExample = '',null,paramsExample),
	if(paramsDescription is null or paramsDescription = '',null,paramsDescription),
	minLength,
	maxLength,
	if(regularExpress is null or regularExpress = '',null,regularExpress),
	status,
	createTime,
	updateTime
FROM
	request_params
WHERE
	apiId=$api_id
and
	status = 1;"

mysql -h${host} -u${user} -p${passwd} -P${port} -D${dbName} -NBe"set names utf8;$sql8" | while read line; do
  result=`echo $line | sed 's/[ ]/|/g'`
  i=2
  request_insert="INSERT INTO request_params (apiId,paramName,paramsType,paramsLocation,paramsMust,paramsDefaultValue,paramsExample,paramsDescription,minLength,maxLength,regularExpress,status,createTime,updateTime)VALUES(@result,"
  while ((1))
  do
    s=`echo $result | cut -d "|" -f $i`
    if [[  "$s" != "" ]] ; then
      request_insert=${request_insert}\'${s}\'','
      (( i++ ))
    else
      break
    fi
  done
  request_insert=`echo $request_insert | sed "s/'NULL'/''/g"`
  echo `echo $request_insert |sed 's/,$//'`');' >> ./insert_api.sql
done

echo "-----------request_params finishi------"





echo "-----------backend_request_params begin--------"

sql9="SELECT
	apiId,
	if(requestParamsId is null or requestParamsId = '','empty',requestParamsId),
	paramsType,
	paramName,
	if(paramValue is null or paramValue = '',null,paramValue),
	paramsLocation,
	if(paramDescription is null or paramDescription = '',null,paramDescription),
        '1',
	createTime,
	updateTime
FROM
	backend_request_params
WHERE
	apiId = $api_id
AND 
	status = 1;"

mysql -h${host} -u${user} -p${passwd} -P${port} -D${dbName} -NBe"set names utf8;$sql9" | while read line; do
  result=`echo $line | sed 's/[ ]/|/g'`
  i=2
  backend_insert="INSERT INTO backend_request_params (apiId, requestParamsId, paramsType, paramName, paramValue, paramsLocation, paramDescription, status, createTime, updateTime) VALUES (@result,"
  while ((1))
  do
    s=`echo $result | cut -d "|" -f $i`
    if [[  "$s" != "" ]] ; then
      backend_insert=${backend_insert}\'${s}\'','
      (( i++ ))
    else
      break
    fi
  done
  backend_insert=`echo $backend_insert | sed "s/'NULL'/''/g"`
  backend_insert=`echo $backend_insert | sed "s/'empty'/NULL/g"`
  echo `echo $backend_insert |sed 's/,$//'`');' >> ./insert_api.sql
done
echo "-----------backend_request_params begin--------"

rm -f ./text.txt
echo "update backend_request_params a, request_params b set a.requestParamsId = b.id WHERE  a.paramName = b.paramName and a.status= b.status and b.status=1 and a.apiId = b.apiId and a.apiId = @result;" >> ./insert_api.sql

done
