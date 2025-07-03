#!/usr/bin/env bash

# 프로젝트 경로
PROJECT_ROOT="/home/ubuntu/ptip2.0_backend"
OLD_DIR="$PROJECT_ROOT/old"
DEPLOY_LOG="$PROJECT_ROOT/deploy.log"
TIME_NOW=$(date +%c)

# old 디렉토리 없으면 생성
mkdir -p "$OLD_DIR"

# 기존 jar 백업
if [ -f "$PROJECT_ROOT/spring-webapp.jar" ]; then
  echo "$TIME_NOW > 기존 JAR 백업 시작" >> $DEPLOY_LOG
  mv "$PROJECT_ROOT/spring-webapp.jar" "$OLD_DIR/spring-webapp-$(date +%Y%m%d%H%M%S).jar"
  echo "$TIME_NOW > spring-webapp.jar 백업 완료" >> $DEPLOY_LOG
else
  echo "$TIME_NOW > 백업할 기존 JAR 없음" >> $DEPLOY_LOG
fi

# 오래된 로그 정리 (선택)
find "$PROJECT_ROOT" -name "*.log" -type f -mtime +14 -exec rm -f {} \;
echo "$TIME_NOW > 14일 초과 로그 파일 정리 완료" >> $DEPLOY_LOG
