#![no_main]

use exports::modulewise::demo::flights;
use modulewise::demo::rest_client;

wit_bindgen::generate!({
    path: "../wit",
    world: "flights-world",
    generate_all,
    additional_derives: [serde::Serialize, serde::Deserialize],
});

#[derive(serde::Serialize, serde::Deserialize)]
struct FlightSearchRequest {
    origin: String,
    destination: String,
    departure: String,
    arrival: Option<String>,
    flex: Option<u8>,
}

struct Flights;

impl Flights {
    fn base_url() -> String {
        wasi::config::store::get("base_url")
            .ok()
            .flatten()
            .unwrap_or("http://localhost:8080".to_string())
    }
}

impl flights::Guest for Flights {
    fn get_flight_by_id(id: String) -> Option<flights::Flight> {
        let url = format!("{}/flights/{id}", Self::base_url());
        match rest_client::get(&url, &[]) {
            Ok(response) => serde_json::from_str(&response).ok(),
            Err(_) => None,
        }
    }

    fn get_flights() -> Vec<flights::Flight> {
        let url = format!("{}/flights", Self::base_url());
        match rest_client::get(&url, &[]) {
            Ok(response) => serde_json::from_str(&response).unwrap_or_else(|_| vec![]),
            Err(_) => vec![],
        }
    }

    fn search_flights(
        origin: String,
        destination: String,
        departure: String,
        arrival: Option<String>,
        flex: Option<u8>,
    ) -> Vec<flights::Flight> {
        let search_request = FlightSearchRequest {
            origin,
            destination,
            departure,
            arrival,
            flex,
        };
        let url = format!("{}/flights/search", Self::base_url());
        let json = serde_json::to_string(&search_request).unwrap_or_default();
        let headers = vec![("Content-Type".to_string(), "application/json".to_string())];
        match rest_client::post(&url, &headers, &json) {
            Ok(response) => serde_json::from_str(&response).unwrap_or_else(|_| vec![]),
            Err(_) => vec![],
        }
    }
}

export!(Flights);
