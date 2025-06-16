#!/usr/bin/env bash

IS_BLUE=$(docker compose ps | grep yapp-blue)
DEFAULT_CONF="data/nginx/nginx.conf"
MAX_RETRIES=60

check_service() {
  local RETRIES=0
  local SERVICE_NAME=$1

  local container_ids=($(docker compose ps -q $SERVICE_NAME))

  while [ $RETRIES -lt $MAX_RETRIES ]; do
    echo "Checking service $SERVICE_NAME (attempt: $((RETRIES+1)))"
    sleep 3

    local all_healthy=true

    for id in "${container_ids[@]}"; do
      local health_status
      health_status=$(docker container inspect --format='{{.State.Health.Status}}' "$id")
      echo "Health status of container $id: $health_status"
      if [ "$health_status" != "healthy" ]; then
        all_healthy=false
        break
      fi
    done

    if [ "$all_healthy" = true ]; then
      echo "$SERVICE_NAME health check passed."
      return 0
    fi

    RETRIES=$((RETRIES+1))
  done

  echo "Failed to check service $SERVICE_NAME after $MAX_RETRIES attempts."
  return 1
}

ensure_nginx_running() {
  local nginx_exists
  nginx_exists=$(docker compose ps -q nginx)
  if [ -z "$nginx_exists" ]; then
    echo "nginx 컨테이너가 존재하지 않습니다. nginx 컨테이너를 실행합니다."
    docker compose up -d nginx
    sleep 5
  else
    echo "nginx 컨테이너가 이미 실행 중입니다."
  fi
}

ensure_promtail_running() {
  local promtail_exists
  promtail_exists=$(docker compose ps -q promtail)
  if [ -z "$promtail_exists" ]; then
    echo "promtail 컨테이너가 존재하지 않습니다. promtail 컨테이너를 실행합니다."
    docker compose up -d promtail
    sleep 5
  else
    echo "promtail 컨테이너가 이미 실행 중입니다."
  fi
}

restart_nginx() {
  echo "nginx 컨테이너를 재시작합니다."
  docker compose restart nginx
}

if [ -z "$IS_BLUE" ]; then
  echo "### GREEN => BLUE ###"

  echo "1. BLUE 컨테이너 실행"
  docker compose up -d yapp-blue --scale yapp-blue=1

  echo "2. BLUE 컨테이너 헬스 체크"
  if ! check_service "yapp-blue"; then
    echo "BLUE health check failed."
    docker compose stop yapp-blue
    docker compose rm -f yapp-blue
    exit 1
  fi

  echo "3. nginx 재실행"
  ensure_nginx_running
  sudo cp -f /home/yapp/data/nginx/nginx-blue.conf /home/yapp/data/nginx/nginx.conf
  restart_nginx

  echo "4. GREEN 컨테이너 중지 및 삭제"
  docker compose stop yapp-green
  docker compose rm -f yapp-green

  echo "Promtail 컨테이너 확인 및 실행"
  ensure_promtail_running

  echo "불필요한 이미지 정리"
  docker image prune -f || true

else
  echo "### BLUE => GREEN ###"

  echo "1. GREEN 컨테이너 실행"
  docker compose up -d yapp-green --scale yapp-green=1

  echo "2. GREEN 컨테이너 헬스 체크"
  if ! check_service "yapp-green"; then
    echo "GREEN health check failed."
    docker compose stop yapp-green
    docker compose rm -f yapp-green
    exit 1
  fi

  echo "3. nginx 재실행"
  ensure_nginx_running
  sudo cp -f /home/yapp/data/nginx/nginx-green.conf /home/yapp/data/nginx/nginx.conf
  restart_nginx

  echo "4. BLUE 컨테이너 중지 및 삭제"
  docker compose stop yapp-blue
  docker compose rm -f yapp-blue

  echo "Promtail 컨테이너 확인 및 실행"
  ensure_promtail_running

  echo "불필요한 이미지 정리"
  docker image prune -f || true
fi
