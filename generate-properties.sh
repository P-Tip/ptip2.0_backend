#!/bin/bash

echo "📦 Generating application.properties from .env..."

# .env 파일 불러오기
set -a
source .env
set +a

mkdir -p src/main/resources

cat <<EOF > src/main/resources/application-prod.properties
spring.application.name=ptip2.0_backend
spring.jackson.time-zone=Asia/Seoul

server.port=$SERVER_PORT

spring.datasource.url=$SPRING_DATASOURCE_URL
spring.datasource.username=$SPRING_DATASOURCE_USERNAME
spring.datasource.password=$SPRING_DATASOURCE_PASSWORD
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect

jwt.secret=$JWT_SECRET
jwt.access-token-expiration=$JWT_ACCESS_TOKEN_EXPIRATION
jwt.refresh-token-expiration=$JWT_REFRESH_TOKEN_EXPIRATION

kakao.client-id=$KAKAO_CLIENT_ID
kakao.client-secret=$KAKAO_CLIENT_SECRET
kakao.redirect-uri=$KAKAO_REDIRECT_URI

springdoc.swagger-ui.path=$SPRINGDOC_SWAGGER_UI_PATH
springdoc.api-docs.path=$SPRINGDOC_API_DOCS_PATH
springdoc.default-consumes-media-type=application/json
springdoc.default-produces-media-type=application/json
springdoc.swagger-ui.enabled=true
springdoc.swagger-ui.disable-swagger-default-url=true

server.forward-headers-strategy=framework
EOF

echo "✅ application-prod.properties 생성 완료!"
