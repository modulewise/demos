#![no_main]

use componentized::valkey::store::{self as valkey, HelloOpts};

wit_bindgen::generate!({
    path: "../wit",
    world: "publisher-world",
    generate_all
});

struct Publisher;

impl exports::modulewise::demo::publisher::Guest for Publisher {
    fn publish(message: String) {
        let opts = HelloOpts {
            proto_ver: Some("3".to_string()),
            auth: None,
            client_name: None,
        };
        let connection =
            valkey::connect("127.0.0.1", 6379, Some(&opts)).expect("failed to connect to valkey");
        connection
            .publish("modulewise", &message)
            .expect("failed to publish message");
    }
}

export!(Publisher);
