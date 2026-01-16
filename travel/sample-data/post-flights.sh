#!/bin/sh

curl -v -d @flights.json -H "Content-Type: application/json" http://localhost:8080/flights
