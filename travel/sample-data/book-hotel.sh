#!/bin/sh

# Book a hotel using the lightweight map-based API
# Usage: ./book_hotel.sh [hotelId] [personId] [checkin] [checkout]
# If no arguments provided, uses default values from sample data

if [ $# -ne 0 ] && [ $# -ne 4 ]; then
    echo "Usage: $0 [hotelId] [personId] [checkin] [checkout]"
    echo "Either provide no arguments (uses defaults) or all four arguments"
    exit 1
fi

HOTEL_ID=${1:-"1"}
PERSON_ID=${2:-"1"}
CHECKIN=${3:-"2025-12-01"}
CHECKOUT=${4:-"2025-12-03"}

echo "Booking hotel ${HOTEL_ID} for person ${PERSON_ID} from ${CHECKIN} to ${CHECKOUT}..."

curl -v -X POST \
  -H "Content-Type: application/json" \
  -d "{\"hotelId\": \"${HOTEL_ID}\", \"personId\": \"${PERSON_ID}\", \"checkin\": \"${CHECKIN}\", \"checkout\": \"${CHECKOUT}\"}" \
  http://localhost:8080/hotels/bookings
