#!/usr/bin/env bash

# Create the file sink connector
echo "Creating File sink connector"
curl -i -X POST -H "Accept:application/json" -H "Content-Type:application/json" -d @config.json http://localhost:8083/connectors/
