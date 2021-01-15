#!/usr/bin/env bash
#应用程序PID
pid=0

#获取主机hostname
host_name=$HOSTNAME
# service jar location
jar_path=~/microservice/

config_hosts=(
	1,test,172.32.3.210,http://172.32.3.210:8761/eureka/,http://172.32.3.211:8761/eureka/
	2,new-test,172.32.4.90,http://172.32.4.90:8761/eureka/,http://localhost:8761/eureka/
	3,tianfu,172.32.3.175,http://172.32.3.175:8761/eureka/,http://172.32.3.177:8761/eureka/
	4,prod,26.8.12.76,http://26.8.12.56:8761/eureka/,http://26.8.12.120:8761/eureka/
	5,yzj-prod,126.2.1.34,http://126.2.1.34:8761/eureka/,http://126.2.1.37:8761/eureka/
)
if [[ 0 -eq $# ]];then
	echo "请选择要启动的应用环境："
	for con in ${config_hosts[@]};do
	    con_index=$(echo ${con}|cut -d , -f 1)
	    con_env=$(echo ${con}|cut -d , -f 2)
	    echo "		$con_index: $con_env"
	done
	read -p "应用环境：" env
	if [[ ! -n "$env" ]] ; then
		echo "参数不能为空..."
		exit 0
	else
		app_env=${env}
	fi
else
	app_env=$1
fi

#第一列表示序号，第二列表示名称,第三列表示应用在eureka中的instanceId,第四列表示应用监听的端口号(用于强制下线eureka实例)
list=(1,micro-gateway-client-service,micro-http-client-service,6090
      2,micro-web-backend-service,micro-web-backend-service,6070
      3,micro-api-gateway,micro-api-gateway,5560
      4,micro-zuul-web-service,micro-zuul-web-service,5556
      5,micro-server-config,micro-server-config,9998
      6,micro-server-eureka,micro-server-eureka,8889
      7,micro-consumer,micro-consumer,7070
      8,micro-api-health,micro-api-health,8081
      9,web-front,web-front,4567
     10,micro-combination-service,micro-combination-service,6050
     11,micro-gateway-async-service,micro-gateway-async-service,9060
     12,micro-gateway-exit-service,micro-gateway-exit-service,5566
     13,micro-admin-monitor,micro-admin-monitor,8890
     14,micro-charge-service,micro-charge-service,6080
)

for c in ${config_hosts[@]};do
	env_index=$(echo ${c}|cut -d , -f 1)
	env_name=$(echo ${c}|cut -d , -f 2)
	env_host=$(echo ${c}|cut -d , -f 3)
	eureka_host_1=$(echo ${c}|cut -d , -f 4)
	eureka_host_2=$(echo ${c}|cut -d , -f 5)

	if [[ "$app_env" = "$env_index" ]];then
		config_profile=${env_name}
		config_host=${env_host}
		eureka_host=${eureka_host_1},${eureka_host_2}
		if [[ *${config_host}* == ${eureka_host_1} ]];then
			eureka_host_copy=${eureka_host_2}
		else
			eureka_host_copy=${eureka_host_1}
		fi
	fi
done
echo "current environment is: $config_profile"
echo "current config host is: $config_host"
echo "current eureka server is: $eureka_host"
echo "---------------------------------------------------------------------------------------------------"


#获取应用pid
getPID(){
    javaps=`jps -l | grep $1`
    if [[ -n "$javaps" ]] ; then
        pid=`echo ${javaps} | awk '{print $1}'`
    else
        pid=0
    fi
}


#停止应用
shutdown(){
    getPID $1
    if [[ ${pid} -ne 0 ]] ; then
        kill -9 ${pid}
        getPID $1
        if [[ ${pid} -eq 0 ]] ; then
            echo "[success]:$1 shutdown success... "
        else
            echo "[fail]:shutdown $1 failed,please check 'defunct' progress..."
        fi
    else
       echo "[warn]:$1 is not running..."
    fi
}

#下线eureka注册实例
offline(){
    url1=$(echo ${eureka_host}|cut -d , -f 1)apps/$2/$1:$2:$3
    curl -s -l -m 10 -o /dev/null -X DELETE ${url1}
    url2=$(echo ${eureka_host}|cut -d , -f 2)apps/$2/$1:$2:$3
    curl -s -l -m 10 -o /dev/null -X DELETE ${url2}
    return 0
}

echo "请选择要停止的应用：
        0: 全部停止"
for f in $(find ~/microservice/ -size +20M -name '*.jar');do
	jar_name=${f##*/}
	app_pid=$(jps -l | grep ${jar_name} | awk '{print $1}')
	if [[ -n "$app_pid" ]];then
		fn=${jar_name%%.*}
		n=${fn%-*}
		for app in ${list[@]};do
			a_index=$(echo ${app} |awk -F, '{print $1}')
			a_name=$(echo ${app} |awk -F, '{print $2}')
			if [[ "$n" == "$a_name" ]];then
				echo "        $a_index: $n"
				list_stop[$a_index]=${app}
			fi
		done
	fi
done
echo "        (多个应用使用空格分隔)"
read -p "请选择：" input_params


if [[ ! -n "$input_params" ]] ; then
    echo "参数不能为空..."
    exit 0
fi

for param in ${input_params};do
    for apps in ${list_stop[@]};do
        app_index=`echo ${apps} | cut -d , -f 1`
        app_name=`echo ${apps} | cut -d , -f 2`
        app_instanceId=`echo ${apps} | cut -d , -f 3`
        app_port=`echo ${apps} | cut -d , -f 4`
        #参数是0的话就没有必要去下线eureka上注册的实例了。
        if [[ ${param} -eq 0 ]] ; then
            offline "$host_name" "$app_instanceId" "$app_port"
            shutdown "$app_name"
        elif [[ "$param" -eq "$app_index" ]] ; then
            offline "$host_name" "$app_instanceId" "$app_port"
            if [[ $? -eq 0 ]] ; then
                echo "[success]:offline $host_name:$app_name success"
                getPID "$app_name"
                if [[ ${pid} -ne 0 ]] ; then
                    sleep 1s
                fi
                shutdown "$app_name"
            else
                echo "[fail]:offline $host_name:$app_name failed...please retry..."
                exit 0
            fi
        fi
    done
done
