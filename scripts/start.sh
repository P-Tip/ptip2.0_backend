#!/usr/bin/env bash

PROJECT_ROOT="/home/ubuntu/ptip2.0_backend"
JAR_SOURCE=$(ls $PROJECT_ROOT/build/libs/*.jar | tail -n 1)
JAR_TARGET="$PROJECT_ROOT/spring-webapp.jar"

APP_LOG="$PROJECT_ROOT/app.log"
ERROR_LOG="$PROJECT_ROOT/error.log"
DEPLOY_LOG="$PROJECT_ROOT/deploy.log"
TIME_NOW=$(date +%c)
PORT=8080

echo "$TIME_NOW > 새 애플리케이션 배포 시작" >> $DEPLOY_LOG

if [ ! -f "$JAR_SOURCE" ]; then
  echo "$TIME_NOW > JAR 파일이 존재하지 않아 복사 실패!" >> $DEPLOY_LOG
  exit 1
fi

echo "$TIME_NOW > JAR 복사: $JAR_SOURCE -> $JAR_TARGET" >> $DEPLOY_LOG
cp "$JAR_SOURCE" "$JAR_TARGET"

chmod +x "$JAR_TARGET"
echo "$TIME_NOW > 실행 권한 부여 및 애플리케이션 실행" >> $DEPLOY_LOG

# 백그라운드 실행
sudo nohup java -Duser.timezone=Asia/Seoul -jar "$JAR_TARGET" > "$APP_LOG" 2> "$ERROR_LOG" &

sleep 5

# 포트 점유 상태 확인
if sudo lsof -i :$PORT -sTCP:LISTEN > /dev/null; then
  echo "$TIME_NOW > 포트 $PORT 정상적으로 점유됨. 실행 성공 추정" >> $DEPLOY_LOG
  sudo lsof -i :$PORT -sTCP:LISTEN >> $DEPLOY_LOG
else
  echo "$TIME_NOW > 포트 $PORT 점유 안됨. 실행 실패 가능성 있음" >> $DEPLOY_LOG
  tail -n 20 "$ERROR_LOG" >> $DEPLOY_LOG
fi
