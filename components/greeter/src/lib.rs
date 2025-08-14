#![no_main]

wit_bindgen::generate!({
    path: "../wit",
    world: "greeter-world",
    generate_all
});

const GREETING_KEY: &str = "greeting";
const DEFAULT_GREETING: &str = "Hello";

struct Greeter;

impl exports::modulewise::demo::greeter::Guest for Greeter {
    fn greet(name: String) -> String {
        let greeting = wasi::config::store::get(GREETING_KEY)
            .ok()
            .flatten()
            .unwrap_or(DEFAULT_GREETING.to_string());
        format!("{greeting} {name}!")
    }
}

export!(Greeter);
