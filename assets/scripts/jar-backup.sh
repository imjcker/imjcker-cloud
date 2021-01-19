script_path=$PWD

jar_path=$script_path/../

bak_path=$jar_path/bak

date=$(date +"%Y%m%d")

if [ ! -d $bak_path/$date ] ; then
  mkdir -p $bak_path/$date
  echo "创建备份路径："
  echo "[ $bak_path/$date ]"
fi

echo "=====开始备份====="

cp -f $jar_path/*.jar $bak_path/$date

zip -q -j $bak_path/$date/$date.zip $bak_path/$date/*

echo "已经备份到 $bak_path/$date/$date.zip"

rm -f $bak_path/$date/*.jar

echo "=====备份完成====="
