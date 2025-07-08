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

#jwt.secret=$JWT_SECRET
#jwt.expiration=$JWT_EXPIRATION
#
#springdoc.swagger-ui.path=$SPRINGDOC_SWAGGER_UI_PATH
#springdoc.api-docs.path=$SPRINGDOC_API_DOCS_PATH
#springdoc.default-consumes-media-type=application/json
#springdoc.default-produces-media-type=application/json
#springdoc.swagger-ui.enabled=true
#springdoc.swagger-ui.disable-swagger-default-url=true
EOF

echo "✅ application-prod.properties 생성 완료!"
