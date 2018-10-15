#!/usr/bin/env bash

# Create the kstream destination topic
echo "Creating KStream destination topic"
docker exec -it broker /usr/bin/kafka-topics --create --topic kstream.demo.transactions --partitions 1 --replication-factor 1 --zookeeper zookeeper:2181

# Build the kstream client
echo "Building KStream client"
cd ../../sources && mvn clean install