#!/bin/sh

# Book a flight using the lightweight map-based API
# Usage: ./book_flight.sh [flightId] [personId]
# If no arguments provided, uses default IDs from sample data

if [ $# -ne 0 ] && [ $# -ne 2 ]; then
    echo "Usage: $0 [flightId] [personId]"
    echo "Either provide no arguments (uses defaults) or both arguments"
    exit 1
fi

FLIGHT_ID=${1:-"1"}
PERSON_ID=${2:-"1"}

echo "Booking flight ${FLIGHT_ID} for person ${PERSON_ID}..."

curl -v -X POST \
  -H "Content-Type: application/json" \
  -d "{\"flightId\": \"${FLIGHT_ID}\", \"personId\": \"${PERSON_ID}\"}" \
  http://localhost:8080/flights/bookings
