#!/bin/sh

curl -v -d @hotels.json -H "Content-Type: application/json" http://localhost:8080/hotels
