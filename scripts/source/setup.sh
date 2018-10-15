#!/usr/bin/env bash

# Create all necessary tables 
echo "Creating MySQL tables."
docker exec -i db-master mysql -u root -pconfluent < tables.sql

# Create required topics that matches 
echo "Creating all source topics."
docker exec -it broker /usr/bin/kafka-topics --create --topic kstream.demo.db.clients --partitions 1 --replication-factor 1 --zookeeper zookeeper:2181
docker exec -it broker /usr/bin/kafka-topics --create --topic kstream.demo.db.addresses --partitions 1 --replication-factor 1 --zookeeper zookeeper:2181
docker exec -it broker /usr/bin/kafka-topics --create --topic kstream.demo.db.transactions --partitions 1 --replication-factor 1 --zookeeper zookeeper:2181

# Create the CDC Sink connector
echo "Creating CDC Sink connector."
curl -i -X POST -H "Accept:application/json" -H "Content-Type:application/json" -d @config.json http://localhost:8083/connectors/