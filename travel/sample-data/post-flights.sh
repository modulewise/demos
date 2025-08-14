#!/bin/sh

curl -v -d @flights_2025-10-01_2025-10-10.json -H "Content-Type: application/json" http://localhost:8080/flights
