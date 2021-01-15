#!/usr/bin/env bash
# author: thh 2019.12

# jmx
jmx_config="-Djava.rmi.server.hostname=localhost -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.port="
# skywalking agent
skywalking_agent="-javaagent:/home/inmgr/apache-skywalking-apm-6.5.0/apache-skywalking-apm-bin/agent/skywalking-agent.jar -Dskywalking.agent.service_name="

#环境配置,依次为：序号，环境，config配置，eureka配置
config_hosts=(
	1,test,172.32.3.210,http://172.32.3.210:8761/eureka/,http://172.32.3.211:8761/eureka/
	2,new-test,172.32.6.25,http://172.32.6.25:8761/eureka/,http://localhost:8761/eureka/
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

#第一列表示序号，第二列表示名称，第三列表示启动是否需要参数。序号严格按照数组下标排列
#list[0]=0,micro-mock-service,0
list[1]=1,micro-gateway-client-service,1,6090
list[2]=2,micro-web-backend-service,1,6070
list[3]=3,micro-api-gateway,1,5560
list[4]=4,micro-zuul-web-service,0,5556
list[5]=5,micro-server-config,0,9998
list[6]=6,micro-server-eureka,0,8889
list[7]=7,micro-consumer,1,7070
#list[8]=8,micro-api-health,0
list[9]=9,web-front,0,4567
list[10]=10,micro-combination-service,1,6050
list[11]=11,micro-gateway-async-service,1,9060
list[12]=12,micro-gateway-exit-service,1,5566
list[13]=13,micro-admin-monitor,0,8890
list[14]=14,micro-charge-service,1,6080


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

#eureka实例所在hostname
instance_hostname=$HOSTNAME
#当前脚本路径
script_path=$(cd `dirname $0` ; pwd)
echo ${script_path}
#应用程序PID
pid=0

#获取应用pid
getPID(){
    javaps=`jps -l | grep $1`
    if [[ -n "$javaps" ]] ; then
        pid=`echo ${javaps} | awk '{print $1}'`
    else
        pid=0
    fi
}

#启动应用
startUp(){
    getPID $1
    if [[ ${pid} -ne 0 ]] ; then
        echo "$1 already started PID=[$pid]....please shut down first..."
    else
        nohup java $2 ${skywalking_agent}$1 -jar ${script_path}/../$1'-0.0.1-SNAPSHOT'.jar $3 >/dev/null 2>&1 &
		sleep 1s
        getPID $1
        if [[ ${pid} -ne 0 ]] ; then
            echo "$1 start succeed PID=[$pid]"
        else
            echo "$1 start failed PID=[$pid]"
        fi
    fi

}

#是否是eureka所在服务器
is_eureka_server=$(find ~/microservice/ -name 'micro-server-eureka*.jar' | wc -l)
# 检查eureka是否启动完成
eureka_pid=$(jps |grep micro-server-eureka |awk '{print $1}')
if [[ "" = "$eureka_pid" ]];then
	if [[ ${is_eureka_server} -ge 1 ]];then
		echo "Eureka Server not started yet, try to start micro-server-eureka"
		startUp "micro-server-eureka" "" "--config.profile=$config_profile --eureka.host=$eureka_host --instance.hostname=$instance_hostname"
	else
		eureka_server_list=(`echo ${eureka_host} |sed s/,/' '/g`)
		for s in ${eureka_server_list[@]}; do
			r=$(curl -l -m 10 -o /dev/null -s -w %{http_code} ${s}apps)
			if [[ 200 = ${r} ]];then
				echo "$s is active"
			else
				echo "$s is dead."
			fi
		done
	fi
else
	echo "当前服务器的eureka已经启动。"
fi
#是否是config所在服务器
is_config_server=$(find ~/microservice/ -name 'micro-server-config*.jar' | wc -l)
# 检查config-server是否注册
config_pid=$(jps |grep micro-server-config |awk '{print $1}')
if [[ "" = "$config_pid" ]];then
	if [[ ${is_config_server} -ge 1 ]];then
		echo "Config Server not started yet, try to start micro-server-config"
		startUp "micro-server-config" "" "--config.profile=$config_profile --eureka.host=$eureka_host --instance.hostname=$instance_hostname"
		for((i=1;i<=30;i++));do
			config=$(curl -l -m 10 -o /dev/null -s -w %{http_code} "$eureka_host_copy"apps/micro-server-config)
			if [[ 200 != ${config} ]];then
				echo "Config Server not registry to Eureka yet: $i"
				sleep 1s
			else
				echo "registry succeed."
				break
			fi
		done
	else
		config=$(curl -l -m 10 -o /dev/null -s -w %{http_code} "$eureka_host_copy"apps/micro-server-config)
		if [[ 200 != ${config} ]];then
			echo "Config Server not registry to Eureka yet, please registry at least one node. "
			exit 0
		else
			echo "found config server registry on eureka server."
		fi
	fi
else
	echo "当前所在服务的config-server已经启动。"
fi
echo "---------------------------------------------------------------------------------------------------"
# 获取没有启动的服务
cd ${script_path}/../
echo  "请选择要启动的应用：
        0: 全部启动"
for f in $(find ~/microservice/ -size +20M -name '*.jar');do
	jar_name=${f##*/}
	app_pid=$(jps -l | grep ${jar_name} | awk '{print $1}')
	if [[ ! -n "$app_pid" ]];then
		fn=${jar_name%%.*}
		n=${fn%-*}
		for app in ${list[@]};do
			a_index=$(echo ${app} |awk -F, '{print $1}')
			a_name=$(echo ${app} |awk -F, '{print $2}')
			if [[ "$n" == "$a_name" ]];then
				echo "        $a_index: $n"
				list_start[$a_index]=${app}
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

for param in ${input_params}
do
	for apps in ${list_start[@]}
	do
		app_index=`echo ${apps}|cut -d , -f 1`
		app_name=`echo ${apps}|cut -d , -f 2`
		need_opt=`echo ${apps}|cut -d , -f 3`
		j_p=1`echo ${apps}|cut -d , -f 4`
        if [[ "$app_index" -eq "1" || "$app_index" -eq "2" || "$app_index" -eq "3" || "$app_index" -eq "10" || "$app_index" -eq "11" || "$app_index" -eq "12" ]] ; then
            #JVM参数
            JVM_OPTS="-Xms3072m -Xmx6144m -Xss256K -XX:MetaspaceSize=64m -XX:MaxMetaspaceSize=128m -Xmn2000m -XX:SurvivorRatio=6 -XX:+UseParNewGC -XX:ParallelGCThreads=8 -XX:+UseConcMarkSweepGC -XX:MinHeapFreeRatio=40 -XX:MaxHeapFreeRatio=70 -XX:CMSInitiatingOccupancyFraction=70 -XX:CMSFullGCsBeforeCompaction=3 -XX:+UseCMSCompactAtFullCollection -XX:+PrintGCDateStamps -Dorg.apache.cxf.JDKBugHacks.gcRequestLatency=true -XX:+PrintGCDetails -Xloggc:$script_path/../logs/gclog/${app_name}-gclog.log -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=${script_path}/dumpFile/ $jmx_config$j_p"
        else
            #JVM参数
            JVM_OPTS="-Xms2048m -Xmx2048m -Xss256K -XX:MetaspaceSize=64m -XX:MaxMetaspaceSize=128m -Xmn1500m -XX:SurvivorRatio=4 -XX:+UseParNewGC -XX:ParallelGCThreads=8 -XX:+UseConcMarkSweepGC  -XX:MinHeapFreeRatio=40 -XX:MaxHeapFreeRatio=70 -XX:CMSInitiatingOccupancyFraction=70 -XX:CMSFullGCsBeforeCompaction=3 -XX:+UseCMSCompactAtFullCollection -XX:+PrintGCDateStamps  -Dorg.apache.cxf.JDKBugHacks.gcRequestLatency=true -XX:+PrintGCDetails -Xloggc:$script_path/../logs/gclog/${app_name}-gclog.log -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=${script_path}/dumpFile/ $jmx_config$j_p"
        fi
        #JAVA启动参数
        JAVA_OPTS="--config.profile=$config_profile --eureka.host=$eureka_host --instance.hostname=$instance_hostname"
		if [[ ${param} -eq 0 ]];then
			#判断是否需要加启动参数
			if [[ ${need_opt} -eq 1 ]] ; then
				startUp "$app_name" "$JVM_OPTS" "$JAVA_OPTS"
			else
				 startUp "$app_name" "" "$JAVA_OPTS"
			fi
		elif [[ "$param" -eq "$app_index" ]] ; then
			#判断是否需要加启动参数
			if [[ ${need_opt} -eq 1 ]] ; then
				startUp "$app_name" "$JVM_OPTS" "$JAVA_OPTS"
			else
				startUp "$app_name" "" "$JAVA_OPTS"
			fi
		fi
	done
done
