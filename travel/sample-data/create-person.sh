#!/bin/sh

echo "Creating test persons..."

valkey-cli HSET persons "1" '{"id":"1","firstName":"Jane","lastName":"Doe","flightLoyaltyPrograms":[],"hotelLoyaltyPrograms":[]}'
valkey-cli HSET persons "2" '{"id":"2","firstName":"John","lastName":"Doe","flightLoyaltyPrograms":[],"hotelLoyaltyPrograms":[]}'

echo "Created persons 1 and 2"
