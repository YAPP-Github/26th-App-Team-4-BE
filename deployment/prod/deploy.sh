#!/usr/bin/env bash

set -euo pipefail
#################################################################
# Log
#################################################################

log_info() { echo -e "\033[32m[$(date '+%F %T')] [INFO] $*\033[0m"; }
log_warn() { echo -e "\033[33m[$(date '+%F %T')] [WARN] $*\033[0m"; }
log_error() { echo -e "\033[31m[$(date '+%F %T')] [ERROR] $*\033[0m"; }

#################################################################
# Variable
#################################################################

DOCKER_COMPOSE="docker compose"

BLUE_CONTAINER="fitrun-blue"
GREEN_CONTAINER="fitrun-green"
NGINX_CONTAINER="nginx"
PROMTAIL_CONTAINER="promtail"

MAX_RETRIES=60
MAX_SCALE=1
SLEEP_SECONDS=5

DATA_PATH="data"
NGINX_PATH="${DATA_PATH}/nginx"
NGINX_TEMPLATE_PATH="${NGINX_PATH}/nginx-template.conf"
NGINX_CONF_PATH="./nginx/nginx.conf"
SRC_PROMTAIL_PATH="${DATA_PATH}/promtail/promtail-config.yml"
DST_PROMTAIL_PATH="./promtail/promtail-config.yml"

#################################################################
# Function
#################################################################

switch_container() {
    local target="$1"
    local old="$2"

    log_info "Switching from ${old^^} to ${target^^}"

    run_service "${target}"
    check_health_service "${target}"
    run_nginx "${target}"
    stop_and_remove_container "${old}"
    run_promtail
    prune_images
}

ensure_run_container() {
    local target=$1
    if [ -z "$(${DOCKER_COMPOSE} ps -q "${target}")" ]; then
        log_warn "${target} container not found. Starting it now..."
        ${DOCKER_COMPOSE} up -d "${target}"
        sleep ${SLEEP_SECONDS}
    fi
}

run_nginx() {
    log_info "Starting ${NGINX_CONTAINER^^} container"
    local target=$1

    export SERVICE_NAME=${target}
    envsubst '${SERVICE_NAME}' < "${NGINX_TEMPLATE_PATH}" > "${NGINX_CONF_PATH}"
    ensure_run_container ${NGINX_CONTAINER}
    ${DOCKER_COMPOSE} restart ${NGINX_CONTAINER}
}

run_promtail() {
    log_info "Starting ${PROMTAIL_CONTAINER^^} container"

    if [[ ! -f "${SRC_PROMTAIL_PATH}" ]]; then
        log_error "Source promtail config not found at ${SRC_PROMTAIL_PATH}"
        return 1
    fi

    if [[ ! -f "${DST_PROMTAIL_PATH}" ]]; then
        log_warn "Destination config not found. Copying and starting promtail..."
    fi

    cp  -n "${SRC_PROMTAIL_PATH}" "${DST_PROMTAIL_PATH}"

    ensure_run_container "${PROMTAIL_CONTAINER}"

    local src_conf_hash
    src_conf_hash=$(sha256sum "${SRC_PROMTAIL_PATH}" | awk '{ print $1 }')

    local dst_conf_hash
    dst_conf_hash=$(sha256sum "${DST_PROMTAIL_PATH}" | awk '{ print $1 }')

    if [[ "${src_conf_hash}" != "${dst_conf_hash}" ]]; then
        log_warn "Config changed. Restarting promtail..."
        cp "${SRC_PROMTAIL_PATH}" "${DST_PROMTAIL_PATH}"
        ${DOCKER_COMPOSE} restart "${PROMTAIL_CONTAINER}"
    fi
}

run_service() {
    local target=$1
    log_info "Starting ${target^^} container"
    ${DOCKER_COMPOSE} up -d "${target}" --scale "${target}=${MAX_SCALE}"
}

check_health_service() {
    local target=$1
    log_info "Performing health check for ${target^^}"

    local retries=0
    local container_ids=( $(${DOCKER_COMPOSE} ps -q "${target}") )

    while [ $retries -lt $MAX_RETRIES ]; do
        log_info "Checking service ${target} (attempt: $((retries+1)))"
        sleep ${SLEEP_SECONDS}

        local all_healthy=true

        for id in "${container_ids[@]}"; do
            local health_status
            health_status=$(docker container inspect --format='{{.State.Health.Status}}' "$id")
            log_info "Container $id health status: $health_status"
            if [ "$health_status" != "healthy" ]; then
                all_healthy=false
                break
            fi
        done

        if [ "$all_healthy" = true ]; then
            log_info "${target^^} passed health check"
            return 0
        fi

        retries=$((retries + 1))
    done

    log_error "Health check failed for ${target^^} after $MAX_RETRIES attempts"
    stop_and_remove_container "${target}"
    exit 1
}

stop_and_remove_container() {
    local target=$1
    log_info "Stopping and removing ${target^^} container"

    if ${DOCKER_COMPOSE} ps | grep -q "${target}"; then
        log_info "Stopping and removing ${target^^} container"
        ${DOCKER_COMPOSE} stop "${target}" || true
        ${DOCKER_COMPOSE} rm -f "${target}" || true
    else
        log_warn "${target} container not found – skip stop/remove"
    fi
}

prune_images() {
    log_info "Cleaning up unused Docker images"
    docker image prune -f || true
}

#################################################################
# Main
#################################################################
main() {
    if ! ${DOCKER_COMPOSE} ps | grep -q "${BLUE_CONTAINER}"; then
        switch_container "${BLUE_CONTAINER}" "${GREEN_CONTAINER}"
    else
        switch_container "${GREEN_CONTAINER}" "${BLUE_CONTAINER}"
    fi
}

main "$@"
