#!/usr/bin/env python3
"""
Flight Data Generator

Generates sample flight data for the travel demo app.
Airlines and their operating airports:
- BUD (Bloom Air): LAX, SFO, ORD
- AGI (Agent Intelligence Airways): SFO, ORD, ATL  
- MOD (Modular Sky Systems): ORD, ATL, JFK

Usage: python generate-flights.py [FIRST_DAY] [LAST_DAY]
Example: python generate-flights.py 2025-10-01 2025-10-10
"""

import json
import sys
from datetime import datetime, timedelta
from typing import List, Dict, Tuple
import random

# Configuration
FIRST_DAY = "2025-10-01"
LAST_DAY = "2025-10-10"

# Airlines and their operating airports
AIRLINES = {
    "BUD": {"name": "Bloom Air", "airports": ["LAX", "SFO", "ORD"]},
    "AGI": {"name": "Agent Intelligence Airways", "airports": ["SFO", "ORD", "ATL"]},
    "MOD": {"name": "Modular Sky Systems", "airports": ["ORD", "ATL", "JFK"]}
}

# Route frequencies (flights per day per airline per route)
ROUTE_FREQUENCIES = {
    # BUD routes
    ("BUD", "LAX", "SFO"): 5,  # High frequency popular route
    ("BUD", "LAX", "ORD"): 3,  # Medium frequency
    ("BUD", "SFO", "ORD"): 2,  # Lower frequency
    
    # AGI routes  
    ("AGI", "SFO", "ORD"): 4,  # High frequency
    ("AGI", "SFO", "ATL"): 2,  # Medium frequency
    ("AGI", "ORD", "ATL"): 3,  # Medium frequency
    
    # MOD routes
    ("MOD", "ORD", "ATL"): 4,  # High frequency
    ("MOD", "ORD", "JFK"): 3,  # Medium frequency
    ("MOD", "ATL", "JFK"): 2,  # Lower frequency
}

# Flight number patterns for easy recall
FLIGHT_NUMBERS = {
    "BUD": ["123", "456", "789", "321", "654"],
    "AGI": ["123", "456", "789", "333", "555"], 
    "MOD": ["123", "456", "789", "321", "555"]
}

# Airport time zones (UTC offset in hours)
AIRPORT_UTC_OFFSETS = {
    "LAX": -8,  # Pacific Time (PST)
    "SFO": -8,  # Pacific Time (PST)
    "ORD": -6,  # Central Time (CST)
    "ATL": -5,  # Eastern Time (EST)
    "JFK": -5   # Eastern Time (EST)
}

# Realistic flight durations (in hours) between airports
FLIGHT_DURATIONS = {
    ("LAX", "SFO"): 1.5,   # 90 minutes
    ("LAX", "ORD"): 4.0,   # 4 hours
    ("SFO", "ORD"): 4.5,   # 4.5 hours
    ("SFO", "ATL"): 4.5,   # 4.5 hours
    ("ORD", "ATL"): 2.0,   # 2 hours
    ("ORD", "JFK"): 2.5,   # 2.5 hours
    ("ATL", "JFK"): 2.5,   # 2.5 hours
}

def get_flight_duration(origin: str, destination: str) -> float:
    """Get realistic flight duration between two airports."""
    # Check both directions
    if (origin, destination) in FLIGHT_DURATIONS:
        return FLIGHT_DURATIONS[(origin, destination)]
    elif (destination, origin) in FLIGHT_DURATIONS:
        return FLIGHT_DURATIONS[(destination, origin)]
    else:
        # Default fallback
        return 3.0

def generate_flight_times(origin: str, destination: str, frequency: int, seed: int, date_str: str) -> List[Tuple[str, str]]:
    """Generate realistic departure and arrival times for a route with proper time zones."""
    random.seed(seed)
    times = []
    
    origin_offset = AIRPORT_UTC_OFFSETS[origin]
    destination_offset = AIRPORT_UTC_OFFSETS[destination]
    duration_hours = get_flight_duration(origin, destination)
    
    # Distribute flights throughout the day (6am - 11pm)
    start_hour = 6
    end_hour = 23
    hours_range = end_hour - start_hour
    
    for i in range(frequency):
        # Spread flights evenly with some randomness
        base_hour = start_hour + (i * hours_range // frequency)
        actual_hour = base_hour + random.randint(-1, 1)
        actual_hour = max(start_hour, min(end_hour, actual_hour))
        
        departure_minute = random.choice([0, 15, 30, 45])  # Realistic departure times
        
        # Create departure time in origin timezone
        departure_dt = datetime.strptime(f"{date_str} {actual_hour:02d}:{departure_minute:02d}", "%Y-%m-%d %H:%M")
        
        # Calculate arrival time by adding flight duration and timezone difference
        time_zone_diff = destination_offset - origin_offset
        arrival_dt = departure_dt + timedelta(hours=duration_hours + time_zone_diff)
        
        # Format times
        departure_time = departure_dt.strftime("%H:%M")
        arrival_time = arrival_dt.strftime("%H:%M")
        
        times.append((departure_time, arrival_time))
    
    return times

def generate_flights(first_day: str, last_day: str) -> List[Dict]:
    """Generate all flights for the date range."""
    flights = []
    
    start_date = datetime.strptime(first_day, "%Y-%m-%d")
    end_date = datetime.strptime(last_day, "%Y-%m-%d")
    
    flight_counter = 1
    
    # Generate flights for each route
    for (airline, origin, destination), frequency in ROUTE_FREQUENCIES.items():
        flight_numbers = FLIGHT_NUMBERS[airline]
        
        current_date = start_date
        while current_date <= end_date:
            date_str = current_date.strftime("%Y-%m-%d")
            
            # Generate times for this route on this day (deterministic based on date)
            seed = hash(f"{airline}_{origin}_{destination}_{date_str}") % 1000000
            times = generate_flight_times(origin, destination, frequency, seed, date_str)
            
            for i, (dep_time, arr_time) in enumerate(times):
                flight_num_idx = i % len(flight_numbers)
                flight_number = flight_numbers[flight_num_idx]
                
                flight = {
                    "id": str(flight_counter),
                    "airline": airline,
                    "number": flight_number,
                    "origin": origin,
                    "destination": destination,
                    "departure": f"{date_str} {dep_time}",
                    "arrival": f"{date_str} {arr_time}"
                }
                
                flights.append(flight)
                flight_counter += 1
            
            current_date += timedelta(days=1)
    
    return flights

def main():
    first_day = sys.argv[1] if len(sys.argv) > 1 else FIRST_DAY
    last_day = sys.argv[2] if len(sys.argv) > 2 else LAST_DAY
    
    print(f"Generating flights from {first_day} to {last_day}")
    
    flights = generate_flights(first_day, last_day)
    
    print(f"Generated {len(flights)} flights")
    
    # Write to JSON file
    output_file = f"flights_{first_day}_{last_day}.json"
    with open(output_file, 'w') as f:
        json.dump(flights, f, indent=2)
    
    print(f"Flights saved to {output_file}")

if __name__ == "__main__":
    main()