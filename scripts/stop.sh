#!/usr/bin/env bash

DEPLOY_LOG="/home/ubuntu/server/deploy.log"
TIME_NOW=$(date +%c)

echo "$TIME_NOW > springboot-app 서비스 종료 시도" >> $DEPLOY_LOG

sudo systemctl stop springboot-app

if systemctl is-active --quiet springboot-app; then
  echo "$TIME_NOW > 종료 실패, 강제 종료 시도 필요" >> $DEPLOY_LOG
else
  echo "$TIME_NOW > springboot-app 서비스 정상 종료됨" >> $DEPLOY_LOG
fi

# 혹시 남아있는 java 프로세스 있는지 확인
PID=$(lsof -t -i:8080)

if [ -n "$PID" ]; then
  echo "$TIME_NOW > 포트 8080 열려 있음, PID $PID 강제 종료 시도" >> $DEPLOY_LOG
  kill -9 "$PID"
  echo "$TIME_NOW > PID $PID 강제 종료 완료" >> $DEPLOY_LOG
fi