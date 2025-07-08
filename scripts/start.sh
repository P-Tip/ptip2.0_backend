#!/usr/bin/env bash

DEPLOY_LOG="/home/ubuntu/server/deploy.log"
TIME_NOW=$(date +%c)
JAR_SOURCE=$(ls /home/ubuntu/server/*.jar | tail -n 1)
JAR_TARGET="/home/ubuntu/server/spring-webapp.jar"

echo "$TIME_NOW > 현재 Nginx 상태:" >> $DEPLOY_LOG
sudo systemctl status nginx >> $DEPLOY_LOG

echo "$TIME_NOW > 새 애플리케이션 복사 시작" >> $DEPLOY_LOG

if [ ! -f "$JAR_SOURCE" ]; then
  echo "$TIME_NOW > JAR 파일이 존재하지 않아 복사 실패!" >> $DEPLOY_LOG
  exit 1
fi

cp "$JAR_SOURCE" "$JAR_TARGET"
chmod +x "$JAR_TARGET"
echo "$TIME_NOW > JAR 복사 및 권한 부여 완료" >> $DEPLOY_LOG

echo "$TIME_NOW > springboot-app 서비스 재시작" >> $DEPLOY_LOG
sudo systemctl restart springboot-app
