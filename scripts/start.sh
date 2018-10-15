#!/usr/bin/env bash

CURRENT_DIRECTORY=$PWD

# Verify mvn is installed
if [[ $(type mvn 2>&1) =~ "not found" ]]; then
  echo "\nERROR: Maven is required. Please install Maven.\n"
  exit 1
fi

# Verify node is installed
if [[ $(type node 2>&1) =~ "not found" ]]; then
  echo "\nERROR: NodeJS is required. Please install NodeJS.\n"
  exit 1
fi

# Verify npm is installed
if [[ $(type npm 2>&1) =~ "not found" ]]; then
  echo "\nERROR: NPM is required. Please install NPM.\n"
  exit 1
fi

# Verify Docker memory is increased to at least 8GB
DOCKER_MEMORY=$(docker system info | grep Memory | grep -o "[0-9\.]\+")
if (( $(echo "$DOCKER_MEMORY 7.0" | awk '{print ($1 < $2)}') )); then
  echo "\nWARNING: Did you remember to increase the memory available to Docker to at least 8GB (default is 2GB)? Demo may otherwise not work properly.\n"
  sleep 3
fi

# Stop existing demo Docker containers
sh ./stop.sh

# Building admin web site
cd ./admin/ && sh ./setup.sh

# Bring up Docker Compose
echo "Bringing up Docker Compose"
docker-compose up -d

# Verify Confluent Control Center has started within MAX_WAIT seconds
MAX_WAIT=300
CUR_WAIT=0
echo "Waiting up to $MAX_WAIT seconds for Confluent Control Center to start"
while [[ ! $(docker-compose logs control-center) =~ "Started NetworkTrafficServerConnector" ]]; do
  sleep 10
  CUR_WAIT=$(( CUR_WAIT+10 ))
  if [[ "$CUR_WAIT" -gt "$MAX_WAIT" ]]; then
    echo "\nERROR: The logs in control-center container do not show 'Started NetworkTrafficServerConnector' after $MAX_WAIT seconds. Please troubleshoot with 'docker-compose ps' and 'docker-compose logs'.\n"
    exit 1
  fi
done

# Verify Docker containers started
if [[ $(docker-compose ps) =~ "Exit 137" ]]; then
  echo "\nERROR: At least one Docker container did not start properly, see 'docker-compose ps'. Did you remember to increase the memory available to Docker to at least 8GB (default is 2GB)?\n"
  exit 1
fi

# Verify Docker has the latest connect image
if [[ $(docker-compose logs connect) =~ "server returned information about unknown correlation ID" ]]; then
  echo -e "\nERROR: Please update the connect image with 'docker-compose pull'\n"
  exit 1
fi

cd $CURRENT_DIRECTORY

echo "\nSetting up Source Connector:"
cd ./source && sh ./setup.sh

cd $CURRENT_DIRECTORY

echo "\nSetting up Sink Connector:"
cd ./sink && sh ./setup.sh

cd $CURRENT_DIRECTORY

echo "\nSetting up KStream client:"
cd ./kstream && sh ./setup.sh

cd $CURRENT_DIRECTORY

echo "\nStarting KStream client:"
cd ./kstream && sh ./start.sh
