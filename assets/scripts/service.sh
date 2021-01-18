#!/usr/bin/env bash

red='\033[0;31m'
green='\033[0;32m'
yellow='\033[0;33m'
plain='\033[0m'

# 工作目录
work_dir=/root/spring-cloud-service-parent
# skywalking settings
agent_dir=/root/skywalking/agent/skywalking-agent.jar

cd "$work_dir" || return
echo -e "${yellow}当前工作目录：$(pwd)$plain"

services=$(ls $work_dir)
for service in $services
do
  if [ -d "${work_dir}/${service}" ]; then
      if [ $# == 1 ];then
          jar=$(find "$work_dir" -iname "*$1*.jar")
          if [[ ${jar} =~ $service ]]; then
            echo -e "${green}${jar}${plain}"
            nohup java -javaagent:$agent_dir -Dskywalking.agent.service_name=$service -jar $jar >/dev/null 2>&1 &
            break
          fi
      fi
  fi
done