#![no_main]

use componentized::valkey::store::{self as valkey, HelloOpts};

wit_bindgen::generate!({
    path: "../wit",
    world: "publisher-world",
    generate_all
});

struct Publisher;

impl exports::modulewise::demo::publisher::Guest for Publisher {
    fn publish(message: String) -> Result<i64, String> {
        let host = get_config_value("host", "127.0.0.1");
        let port: u16 = get_config_value("port", "6379")
            .parse()
            .expect("failed to parse port");
        let channel = get_config_value("channel", "modulewise");
        let opts = HelloOpts {
            proto_ver: Some("3".to_string()),
            auth: None,
            client_name: None,
        };
        let connection =
            valkey::connect(&host, port, Some(&opts)).expect("failed to connect to valkey");
        connection
            .publish(&channel, &message)
            .map_err(|e| e.to_string())
    }
}

fn get_config_value(key: &str, default: &str) -> String {
    wasi::config::store::get(&key)
        .ok()
        .flatten()
        .unwrap_or(default.to_string())
}

export!(Publisher);
