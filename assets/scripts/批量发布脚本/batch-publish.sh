#!/bin/bash
#批量发布脚本,apiIdList表示要发布的apiId，格式1 2 3 7-10 22,空格表示一个，-表示区间段; env=1|2表示线上、测试环境发布; publishDescribtion发布描述

#发起请求地址，即发布API的服务器地址，端口为zull-web.不能直接请求web-backend，会被单点登录先验证，无法直接发请求。
address=172.32.3.175:5556

#接口地址,web-backend中定义的接口，Get方式，传入三个参数，分别是apiIdList、env、publishDescribtion
uri=/gateway/gateway/gateway/api/publishAll

if [ $# -le 0 ] ; then
  echo "请输入至少一个api的id"
  exit 0
fi

#获取传入参数apiIdList
apiIdList="$@";

#替换字符串中的空格为%20
apiIdListReplaceSpance=`echo $apiIdList|sed 's/[ ]/%20/g'`

#用于发布的变量,替换空格为逗号，发布描述中不支持空格
apiIdListPubDescription=`echo $apiIdList|sed 's/[ ]/,/g'`
pubDescription="publish""${apiIdListPubDescription}";

#发起批量发布请求
#env=1
curl http://${address}${uri}?apiIdList=$apiIdListReplaceSpance\&env=1\&pubDescription=$pubDescription
echo ""
#env=2
curl http://${address}${uri}?apiIdList=$apiIdListReplaceSpance\&env=2\&pubDescription=$pubDescription
echo ""
#echo http://${address}${uri}?apiIdList=$apiIdListReplaceSpance\&env=2\&pubDescription=$pubDescription
