#!/bin/sh

echo ">>> invoking calculator core module..."

wasmtime --invoke add calculator.core.wasm 7 6 2>/dev/null
wasmtime --invoke subtract calculator.core.wasm 7 6 2>/dev/null
wasmtime --invoke multiply calculator.core.wasm 7 6 2>/dev/null
wasmtime --invoke divide calculator.core.wasm 7 6 2>/dev/null

echo "\n>>> invoking calculator component..."

wasmtime --invoke 'add(7, 6)' calculator.wasm
wasmtime --invoke 'subtract(7, 6)' calculator.wasm
wasmtime --invoke 'multiply(7, 6)' calculator.wasm
wasmtime --invoke 'divide(7, 6)' calculator.wasm
