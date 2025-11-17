#!/bin/bash

set -euo pipefail

PROJECTS=`cargo metadata --no-deps --format-version 1 | jq -r '.packages[].name'`

for project in $PROJECTS; do
  cargo component build -p $project --target wasm32-unknown-unknown --release
  cargo_name=$(echo "$project" | tr '-' '_')
  wasm_file="target/wasm32-unknown-unknown/release/${cargo_name}.wasm"
  cp "$wasm_file" "lib/${project}.wasm"
done

( cd calculator; ./componentize.sh; cp calculator.wasm ../lib )

# rely on default greeting "Hello"
static-config -o ./lib/empty-config.wasm
wac plug ./lib/greeter.wasm --plug ./lib/empty-config.wasm -o ./lib/hello.wasm

# provide greeting value through wasi:config/store-exporting component
static-config -p greeting="Aloha" -o ./lib/aloha-config.wasm
wac plug ./lib/greeter.wasm --plug ./lib/aloha-config.wasm -o ./lib/aloha.wasm

wkg oci pull -o ./lib/valkey-client.wasm ghcr.io/componentized/valkey/valkey-client:v0.2.0
wac plug ./lib/incrementor.wasm --plug ./lib/valkey-client.wasm -o ./lib/valkey-incrementor.wasm
wac plug ./lib/valkey-incrementor.wasm --plug ./lib/empty-config.wasm -o ./lib/default-incrementor.wasm

wac plug ./lib/counter.wasm --plug ./lib/valkey-incrementor.wasm -o ./lib/valkey-counter.wasm
