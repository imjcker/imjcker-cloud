#!/usr/bin/env bash
#设置Linux快捷键脚本

alias jl='jps -l'
alias fm='free -m'
alias fg='free -g'
alias tf='tail -100f '

alias tfz='tail -100f ~/logs/micro-api-gateway-info.log'
alias tfze='tail -100f ~/logs/micro-api-gateway-error.log'
alias tfg='tail -100f ~/logs/micro-gateway-client-info.log'
alias tfge='tail -100f ~/logs/micro-gateway-client-error.log'
alias tfa='tail -100f ~/logs/micro-gateway-async-service-info.log'
alias tfae='tail -100f ~/logs/micro-gateway-async-service-error.log'
alias tfc='tail -100f ~/logs/micro-combination-service-info.log'
alias tfce='tail -100f ~/logs/micro-combination-service-error.log'
alias tfe='tail -100f ~/logs/micro-gateway-exit-service-info.log'
alias tfee='tail -100f ~/logs/micro-gateway-exit-service-error.log'
alias tfcon='tail -100f ~/logs/micro-consumer-info.log'
alias tfb='tail -100f ~/logs/micro-web-backend-service-info.log'
alias tff='tail -100f ~/logs/front-web-info.log'
alias tfzw='tail -100f ~/logs/micro-zuul-web-info.log'

# for service start and shutdown
alias shst='sh ~/microservice/scripts/start.sh'
alias shsd='sh ~/microservice/scripts/shutdown.sh'

# tools
alias cc='echo 3 > /proc/sys/vm/drop_caches'
