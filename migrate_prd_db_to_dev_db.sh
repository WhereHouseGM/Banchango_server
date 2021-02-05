#!/bin/bash

DB_HOST=wherehouse.ckaaugdedeew.ap-northeast-2.rds.amazonaws.com 
PRD_DB=banchango
DEV_DB=banchango_2

echo "프로덕션 디비("$PRD_DB") 덤프 중..."
echo "USER: wherehouse 비밀번호 입력"
mysqldump -h $DB_HOST -u wherehouse -p --databases $PRD_DB > $PRD_DB'.sql'

sed -i 's/'$PRD_DB'/'$DEV_DB'/g' $PRD_DB'.sql'

echo "개발용 디비("$DEV_DB") 덤프 중..."
echo "USER: wherehouse 비밀번호 입력"
mysql -h $DB_HOST -u root -p $DEV_DB < $PRD_DB'.sql'

rm $PRD_DB'.sql'
