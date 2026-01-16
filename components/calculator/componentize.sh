#!/bin/sh

wat2wasm calculator.wat -o calculator.core.wasm

wasm-tools component embed ../wit -w calculator calculator.core.wasm | \
wasm-tools component new - -o calculator.wasm
