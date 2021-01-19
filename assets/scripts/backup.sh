#!/bin/sh
# 定义忽略的路径和文件
ignore=(
logs
mock
)
target_path=~/microservice
now_date=$(date +"%Y%m%d")
backup_path=$target_path-$now_date
echo $target_path --'>' $backup_path

# 创建备份路径
if [ ! -d $backup_path ] ; then
  echo "备份文件夹不存在，创建中..."
  mkdir -p $backup_path
  sleep 2s
else
	echo "目录已存在，是否覆盖"
	read -p "Y/N：" choice
	to_upper_case=$(echo $choice | tr [a-z] [A-Z])
	echo $to_upper_case
	if [ $to_upper_case = "N" ]; then
		echo "停止备份操作"
		exit 0
	fi
fi

echo "备份开始"
for f in `ls $target_path`
do
	tmp="false"
	for i in ${ignore[@]}
	do
		if [ $f = $i ];then
			echo "忽略："$f
			tmp="true"
			break
		fi
	done
	if [ "true" = $tmp ];then
		continue
	fi
	path=$target_path/$f
	if [ -f "$path" ]; then
		cp -vf $path $backup_path
		sleep 1s
	elif [ -d "$path" ]; then
		echo "备份目录：" $path
		cp -rvf $path $backup_path
		sleep 1s
	fi
done
echo "备份完成"