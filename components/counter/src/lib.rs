#![no_main]

wit_bindgen::generate!({
    path: "../wit",
    world: "counter-world",
    generate_all
});

struct Counter;

impl exports::modulewise::demo::counter::Guest for Counter {
    fn count(thing: String) -> String {
        let count = modulewise::demo::incrementor::increment(&thing).unwrap_or(0);
        let thing = if count == 1 {
            thing
        } else {
            format!("{thing}s")
        };
        let suffix = wasi::config::store::get("suffix")
            .ok()
            .flatten()
            .unwrap_or("!".to_string());
        format!("{count} {thing} {suffix}")
    }
}

export!(Counter);
