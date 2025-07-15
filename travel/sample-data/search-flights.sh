#!/bin/sh

set -x

# Example flight search: LAX to SFO, departure around 9am with 3 hour flexibility
curl -d '{
  "origin": "LAX",
  "destination": "SFO", 
  "departure": "2025-10-05 09:00",
  "flex": 3
}' -H "Content-Type: application/json" http://localhost:8080/flights/search | jq .
