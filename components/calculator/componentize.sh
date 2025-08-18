#!/bin/sh

wat2wasm calculator.wat -o calculator.core.wasm

sed 's/(export "\([^"]*\)")/(export "modulewise:demo\/calculator@0.1.0#\1")/g' calculator.wat | \
wat2wasm /dev/stdin --output=- | \
wasm-tools component embed ../wit -w calculator-world - | \
wasm-tools component new - -o calculator.wasm
