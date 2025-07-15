#!/usr/bin/env python3
"""
Hotel Data Generator

Generates sample hotel data for the travel demo app.
Creates themed hotels in 5 major US cities:
- Los Angeles (LAX area)
- San Francisco (SFO area) 
- Chicago (ORD area)
- Atlanta (ATL area)
- New York (JFK area)

Hotel themes based on project concepts:
- Spring/Season themed hotels
- AI/Agent themed hotels  
- Modularity/Tech themed hotels

Usage: python generate-hotels.py
"""

import json
import random

# Cities mapped to airport codes for consistency
CITIES = {
    "Los Angeles": "LAX",
    "San Francisco": "SFO", 
    "Chicago": "ORD",
    "Atlanta": "ATL",
    "New York": "JFK"
}

# Hotel name templates by theme
HOTEL_NAMES = {
    "spring": [
        "Bloom Suites", "Blossom Hotel", "Spring Garden Inn", "Petal Plaza",
        "Sunrise Resort", "Fresh Air Lodge", "Garden View Hotel", "Bloom Tower",
        "Spring Valley Inn", "Botanical Suites"
    ],
    "agents": [
        "Agent Central Hotel", "Intelligence Suites", "Neural Network Inn", 
        "Algorithm Plaza", "Data Point Hotel", "Logic Gate Lodge", "AI Tower",
        "Cognitive Suites", "Smart Agent Inn", "Binary Hotel"
    ],
    "modular": [
        "Modular Plaza", "Component Hotel", "Interface Inn", "Assembly Suites",
        "Framework Lodge", "Module Tower", "System Hotel", "Plugin Plaza",
        "Architecture Inn", "Builder Suites"
    ]
}

# Hotel descriptions by theme
DESCRIPTIONS = {
    "spring": [
        "A refreshing hotel with garden views and seasonal amenities",
        "Modern comfort meets natural beauty in this botanical-themed property",
        "Experience renewal and growth in our spring-inspired accommodations", 
        "Bright, airy spaces designed to energize and rejuvenate",
        "Where every stay feels like a fresh start"
    ],
    "agents": [
        "Smart technology and intelligent service define this cutting-edge hotel",
        "AI-powered amenities provide personalized experiences for every guest",
        "Advanced automation meets luxury hospitality",
        "Experience the future of travel with our intelligent systems",
        "Where artificial intelligence enhances authentic hospitality"
    ],
    "modular": [
        "Flexible spaces that adapt to your unique travel needs",
        "Innovative design with customizable room configurations",
        "Scalable luxury with component-based amenities",
        "Modular comfort systems for the modern traveler",
        "Built for flexibility, designed for comfort"
    ]
}

def generate_hotels():
    """Generate hotel data for all cities."""
    hotels = []
    hotel_id = 1
    
    for city in CITIES.keys():
        # Generate 7-10 hotels per city
        num_hotels = random.randint(7, 10)
        
        # Mix of themes per city
        themes = ["spring", "agents", "modular"]
        
        for i in range(num_hotels):
            theme = themes[i % len(themes)]
            
            # Pick a name from the theme, ensuring variety
            available_names = [name for name in HOTEL_NAMES[theme]]
            if available_names:
                name = random.choice(available_names)
                # Remove used name to avoid duplicates in same city
                HOTEL_NAMES[theme].remove(name)
            else:
                # Fallback if we run out of names
                name = f"{theme.title()} Hotel {i+1}"
            
            # Star rating (weighted toward 3-4 stars)
            stars = random.choices([2, 3, 4, 5], weights=[10, 40, 35, 15])[0]
            
            # Description
            description = random.choice(DESCRIPTIONS[theme])
            
            hotel = {
                "id": str(hotel_id),
                "name": name,
                "city": city,
                "stars": stars,
                "description": description
            }
            
            hotels.append(hotel)
            hotel_id += 1
    
    return hotels

def main():
    print("Generating hotel data...")
    
    hotels = generate_hotels()
    
    print(f"Generated {len(hotels)} hotels across {len(CITIES)} cities")
    
    # Show breakdown by city
    city_counts = {}
    for hotel in hotels:
        city = hotel["city"]
        city_counts[city] = city_counts.get(city, 0) + 1
    
    print("\nHotels per city:")
    for city, count in city_counts.items():
        print(f"  {city}: {count} hotels")
    
    # Write to JSON file
    output_file = "hotels.json"
    with open(output_file, 'w') as f:
        json.dump(hotels, f, indent=2)
    
    print(f"\nHotels saved to {output_file}")

if __name__ == "__main__":
    main()