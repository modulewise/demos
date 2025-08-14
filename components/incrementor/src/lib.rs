#![no_main]

use wasi::config;
use wasi::keyvalue::{
    atomics,
    store::{self, Error},
};

wit_bindgen::generate!({
    world: "incrementor-world",
    path: "../wit",
    generate_all
});

const DEFAULT_BUCKET_NAME: &str = "counts";

#[derive(Debug, Clone)]
struct Incrementor;

impl Incrementor {
    fn increment(key: &str) -> Result<i64, Error> {
        let bucket_key = config::store::get("bucket")
            .ok()
            .flatten()
            .unwrap_or(DEFAULT_BUCKET_NAME.to_string());
        let bucket = store::open(&bucket_key)?;
        atomics::increment(&bucket, key, 1)
    }
}

impl exports::modulewise::demo::incrementor::Guest for Incrementor {
    fn increment(key: String) -> Result<i64, String> {
        Self::increment(&key).map_err(|e| format!("{e:?}"))
    }
}

export!(Incrementor);
