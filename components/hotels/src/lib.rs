#![no_main]

use exports::modulewise::demo::hotels;
use modulewise::demo::http_client;

wit_bindgen::generate!({
    path: "../wit",
    world: "hotels-world",
    generate_all,
    additional_derives: [serde::Serialize, serde::Deserialize],
});

#[derive(serde::Serialize, serde::Deserialize)]
struct HotelSearchRequest {
    city: String,
    checkin: String,
    checkout: String,
    flex: Option<u8>,
    minstars: Option<u8>,
}

struct Hotels;

impl Hotels {
    fn base_url() -> String {
        wasi::config::store::get("base_url")
            .ok()
            .flatten()
            .unwrap_or("http://localhost:8080".to_string())
    }
}

impl hotels::Guest for Hotels {
    fn get_hotel_by_id(id: String) -> Option<hotels::Hotel> {
        let url = format!("{}/hotels/{id}", Self::base_url());
        match http_client::get(&url, &[]) {
            Ok(response) => serde_json::from_str(&response.body).ok(),
            Err(_) => None,
        }
    }

    fn get_hotels() -> Vec<hotels::Hotel> {
        let url = format!("{}/hotels", Self::base_url());
        match http_client::get(&url, &[]) {
            Ok(response) => serde_json::from_str(&response.body).unwrap_or_else(|_| vec![]),
            Err(_) => vec![],
        }
    }

    fn search_hotels(
        city: String,
        checkin: String,
        checkout: String,
        flex: Option<u8>,
        minstars: Option<u8>,
    ) -> Vec<hotels::Hotel> {
        let search_request = HotelSearchRequest {
            city,
            checkin,
            checkout,
            flex,
            minstars,
        };
        let url = format!("{}/hotels/search", Self::base_url());
        let json = serde_json::to_string(&search_request).unwrap_or_default();
        let headers = vec![("Content-Type".to_string(), "application/json".to_string())];
        match http_client::post(&url, &headers, &json) {
            Ok(response) => serde_json::from_str(&response.body).unwrap_or_else(|_| vec![]),
            Err(_) => vec![],
        }
    }
}

export!(Hotels);
