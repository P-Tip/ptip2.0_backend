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
