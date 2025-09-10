#![no_main]

wit_bindgen::generate!({
    path: "../wit",
    world: "intercepting-greeter-world",
    generate_all
});

struct Greeter;

impl exports::modulewise::demo::greeter::Guest for Greeter {
    fn greet(name: String) -> String {
        let message = modulewise::demo::greeter::greet(&name);
        let prefix = get("prefix");
        let suffix = get("suffix");
        format!("{prefix}{message}{suffix}")
    }
}

fn get(key: &str) -> String {
    wasi::config::store::get(key)
        .ok()
        .flatten()
        .unwrap_or_default()
}

export!(Greeter);
