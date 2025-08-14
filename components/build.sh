#!/bin/bash

set -euo pipefail

cargo component build -p calculator --target wasm32-unknown-unknown --release
cargo component build -p counter --target wasm32-unknown-unknown --release
cargo component build -p flights --target wasm32-unknown-unknown --release
cargo component build -p greeter --target wasm32-unknown-unknown --release
cargo component build -p hotels --target wasm32-unknown-unknown --release
cargo component build -p incrementor --target wasm32-unknown-unknown --release
cargo component build -p rest-client --target wasm32-unknown-unknown --release

cp target/wasm32-unknown-unknown/release/*.wasm lib/

# rely on default greeting "Hello"
static-config -o ./lib/empty-config.wasm
wac plug ./lib/greeter.wasm --plug ./lib/empty-config.wasm -o ./lib/hello.wasm

# provide greeting value through wasi:config/store-exporting component
static-config -p greeting="Aloha" -o ./lib/aloha-config.wasm
wac plug ./lib/greeter.wasm --plug ./lib/aloha-config.wasm -o ./lib/aloha.wasm

wkg oci pull -o ./lib/valkey-client.wasm ghcr.io/componentized/valkey/valkey-client:v0.1.1
wac plug ./lib/incrementor.wasm --plug ./lib/valkey-client.wasm -o ./lib/valkey-incrementor.wasm
wac plug ./lib/valkey-incrementor.wasm --plug ./lib/empty-config.wasm -o ./lib/default-incrementor.wasm

wac plug ./lib/counter.wasm --plug ./lib/valkey-incrementor.wasm -o ./lib/valkey-counter.wasm
