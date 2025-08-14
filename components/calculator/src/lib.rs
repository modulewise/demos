#![no_main]

wit_bindgen::generate!({
    path: "../wit",
    world: "calculator-world",
    generate_all
});

struct Calculator;

impl exports::modulewise::demo::calculator::Guest for Calculator {
    fn add(a: f32, b: f32) -> f32 {
        a + b
    }

    fn subtract(a: f32, b: f32) -> f32 {
        a - b
    }

    fn multiply(a: f32, b: f32) -> f32 {
        a * b
    }

    fn divide(a: f32, b: f32) -> f32 {
        a / b
    }
}

export!(Calculator);
