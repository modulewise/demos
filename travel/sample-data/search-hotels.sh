#!/bin/sh

set -x

# Example hotel search: Hotels in Chicago with minimum 3 stars
curl -d '{
  "city": "Chicago",
  "checkin": "2025-10-05",
  "checkout": "2025-10-07",
  "minStars": 3,
  "flex": 1
}' -H "Content-Type: application/json" http://localhost:8080/hotels/search | jq .
