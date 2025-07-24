#!/usr/bin/env bash

PROJECT_ROOT="/home/ubuntu/server"
OLD_DIR="$PROJECT_ROOT/old"
LOG_DIR="$PROJECT_ROOT/logs"
DEPLOY_LOG="$LOG_DIR/deploy.log"
TIME_NOW=$(date +%c)

# old 디렉토리 없으면 생성
mkdir -p "$OLD_DIR/logs"

# 기존 JAR 백업
if [ -f "$PROJECT_ROOT/spring-webapp.jar" ]; then
  echo "$TIME_NOW > 기존 JAR 백업 시작" >> $DEPLOY_LOG
  mv "$PROJECT_ROOT/spring-webapp.jar" "$OLD_DIR/spring-webapp-$(date +%Y%m%d%H%M%S).jar"
  echo "$TIME_NOW > spring-webapp.jar 백업 완료" >> $DEPLOY_LOG
else
  echo "$TIME_NOW > 백업할 기존 JAR 없음" >> $DEPLOY_LOG
fi

# 로그 백업
for log_file in "$LOG_DIR/app.log" "$LOG_DIR/error.log"; do
  if [ -f "$log_file" ]; then
    mv "$log_file" "$OLD_DIR/logs/$(basename "$log_file" .log)-$(date +%Y%m%d%H%M%S).log"
    echo "$TIME_NOW > $(basename "$log_file") 백업 완료" >> $DEPLOY_LOG
  fi
done

# 오래된 로그 정리 (선택)
find "$OLD_DIR/logs" -name "*.log" -type f -mtime +14 -exec rm -f {} \;
echo "$TIME_NOW > 14일 초과 백업 로그 파일 정리 완료" >> $DEPLOY_LOG
