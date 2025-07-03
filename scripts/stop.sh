#!/usr/bin/env bash

PROJECT_ROOT="/home/ubuntu/ptip2.0_backend"
DEPLOY_LOG="$PROJECT_ROOT/deploy.log"
TIME_NOW=$(date +%c)
PORT=8080

echo "$TIME_NOW > 실행 중인 애플리케이션 종료 시도" >> $DEPLOY_LOG

# 443 포트 LISTEN 중인 PID 탐색
CURRENT_PID=$(sudo lsof -i :$PORT -sTCP:LISTEN -t)

if [ -z "$CURRENT_PID" ]; then
  echo "$TIME_NOW > 포트 $PORT LISTEN 중인 프로세스 없음" >> $DEPLOY_LOG
else
  echo "$TIME_NOW > 포트 $PORT LISTEN 중인 PID $CURRENT_PID 종료 시도" >> $DEPLOY_LOG
  sudo kill -15 "$CURRENT_PID"
  sleep 3

  if ps -p "$CURRENT_PID" > /dev/null; then
    echo "$TIME_NOW > 정상 종료 실패, 강제 종료 시도" >> $DEPLOY_LOG
    sudo kill -9 "$CURRENT_PID"
  fi
fi
