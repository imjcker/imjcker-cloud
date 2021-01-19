#!/bin/sh
# author: thh 2019.11

if [ $# != 3 ];then
	echo "usage: 脚本需3个参数，"
	echo "参数1，文件存放路径，可使用相对路径，举例：./   ~/microservice /root/home/inmgr/ 等"
	echo "参数2，下载类型，-f 下载文件，-d 下载目录"
	echo "参数3，下载文件或目录，可使用通配符，使用相对路径，跟路径在脚本中写死为ftp服务的固定路径，参数3的路径是在此跟路径的基础上的相对路径，举例：20191125 20191125/aaa.jar 20191125/*bbb*.jar"
	echo "完整例子：./ftp_get.sh ./ -f 20191126/*micro*.jar 表示从ftp下的duanlong/inmgr/20191126目录下载所有满足通配符标准的jar包到当前目录"
	echo "完整例子：./ftp_get.sh ./ -f 20191126 表示从ftp下的duanlong/inmgr目录下载整个名为20191126的目录到当前目录下"
	exit 0
fi

now_date=$(date +"%Y%m%d%H%M%S")
# FTP 配置
IP="172.32.3.211"
USER="inmgr"
PASSWORD="inmgr"
# ftp根目录
FTP_BASE_DIR=/microservice
# FTP 下载函数
DownloadFiles(){
    ftp -ivn <<EOF
    open $IP
    user $USER $PASSWORD
    bin
	lcd $1
    cd $FTP_BASE_DIR/$2
    ls
    mget $3
    close
    quit
EOF
}
DownloadFile(){
    ftp -ivn <<EOF
    open $IP
    user $USER $PASSWORD
    bin
	lcd $1
    cd $FTP_BASE_DIR/$2
    ls
    get $3
    close
    quit
EOF
}
cd $1
if [ 0 -ne $? ];then
	echo "本地路径不存在，开始创建本地路径"
	mkdir -p $1
	cd $1
	if [ 0 -eq $? ];then
		echo "创建目录成功：$1"
	else
		echo "创建目录失败，准备退出..."
		sleep 2s
		exit 0
	fi
fi
echo "当前路径为：$PWD"
local_dir=`echo $PWD`
sleep 1s
if [ "-f" = "$2" ];then
	echo "FTP下载文件: $FTP_BASE_DIR/$3"
	sleep 2s
	ftp_file=$3
	dir=${ftp_file%/*}
	file_name=${ftp_file##*/}
	count=`echo $3 |tr -cd '*' | wc -c`
	if [ 0 -eq $count ];then
		echo "精确下载：$FTP_BASE_DIR/$3"
		sleep 2s
		DownloadFile "$local_dir" "$dir" "$file_name"
	else
		echo "模糊下载：$FTP_BASE_DIR/$dir下的：$file_name"
		sleep 3s
		DownloadFiles "$local_dir" "$dir" "$file_name"
	fi
elif [ "-d" = "$2" ];then
	echo "FTP下载目录: $FTP_BASE_DIR/$3"
	sleep 3s
	DownloadFiles "$local_dir" "$3" "*.*"
fi
echo "新下载文件如下======================$PWD============================"
for f in `ls ./`;do
	file_date=$(stat $f|grep Modify|awk '{print $2$3}'|sed s/-//g |awk -F : '{print $1$2$3}'|awk -F . '{print $1}')
	if [ $now_date -le $file_date ]; then
		echo $f
	fi
done
