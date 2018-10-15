#!/usr/bin/env bash

ps aux | grep kstream | grep -v "grep kstream" | awk '{print $2}' | xargs kill -15

docker-compose down

for v in $(docker volume ls -q --filter="dangling=true"); do
	docker volume rm "$v"
done
