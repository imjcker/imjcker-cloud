#!/usr/bin/env bash
######################################
#日志路径根据具体路径对应关系进行更改#
######################################

#脚本路径
#script_path=$(cd `dirname $0` ; pwd)
#日志路径
log_path=~/logs
#日志备份路径
backup_path=$log_path/bak
#3天以前的日期
zip_date=$(date -d "3 days ago" +"%Y%m%d")
#9天以前的日期
delete_date=$(date -d "9 days ago" +"%Y%m%d")
#当前日期
now_date=$(date +"%Y%m%d")

#避免crontab执行路径有问题
#cd $script_path

if [ ! -d $backup_path ] ; then
  mkdir -p $backup_path
  echo "备份文件夹不存在，创建中..."
fi

#日志名称中带有数字的(非当前日志)全部移到bak中
for log in `ls $log_path`
do
  log_name=`echo $log | cut -d . -f 1`
  log_date=`echo $log_name | tr -cd "[0-9]"`
  if [ "$log_date" != "" ] ; then
    if [ ! -d $backup_path/$log_date ] ; then
      mkdir -p $backup_path/$log_date
    fi
    mv $log_path/$log $backup_path/$log_date
  fi
done

#进行压缩打包
for dir in `ls $backup_path`
do
  if [ $dir -le $delete_date ] ; then
    echo "日志日期: $dir 已超过9天，进行删除..."
    echo '=============================='
    #加个判断..避免删库跑路...
    if [ "$backup_path" != "" -a "$dir" != "" ] ; then
        rm -rf $backup_path/$dir
    else
        echo "删除失败...请检查参数是否正确..."
    fi
  elif [ $dir -le $now_date ] ; then
    ls -A $backup_path/$dir | grep zip >/dev/null
    result=`echo $?`
    if [ $result -eq 1 ] ; then 
      echo "开始压缩...当前文件夹:$dir"
      zip -q -j  $backup_path/$dir/$dir.zip $backup_path/$dir/*
      if [ "$backup_path" != "" -a "$dir" != "" ] ; then
        rm -f $backup_path/$dir/*.log*
        echo "压缩完毕..."
        echo '============================='
      else
        echo "压缩失败...请检查参数"
        echo '============================='
      fi
    fi
  fi
done


