#!/bin/bash

set -euo pipefail
set -x

wasmtime --invoke 'greet("World")' lib/hello.wasm
wasmtime --invoke 'greet("World")' lib/aloha.wasm

wasmtime --invoke 'add(3, 4)' lib/calculator.wasm
wasmtime --invoke 'subtract(10, 5)' lib/calculator.wasm
wasmtime --invoke 'multiply(6, 7)' lib/calculator.wasm
wasmtime --invoke 'divide(52, 13)' lib/calculator.wasm
