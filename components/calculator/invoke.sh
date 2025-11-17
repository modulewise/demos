#!/bin/sh

echo ">>> invoking calculator core module..."

wasmtime --invoke add calculator.core.wasm 3 4 2>/dev/null
wasmtime --invoke subtract calculator.core.wasm 10 5 2>/dev/null
wasmtime --invoke multiply calculator.core.wasm 6 7 2>/dev/null
wasmtime --invoke divide calculator.core.wasm 52 13 2>/dev/null

echo "\n>>> invoking calculator component..."

wasmtime --invoke 'add(3, 4)' calculator.wasm
wasmtime --invoke 'subtract(10, 5)' calculator.wasm
wasmtime --invoke 'multiply(6, 7)' calculator.wasm
wasmtime --invoke 'divide(52, 13)' calculator.wasm
