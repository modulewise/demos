# Modulewise Demo Components

Wasm Components used in Modulewise Demos

## Prerequisites

Requires a rust toolchain with the `wasm32-unknown-unknown` target as well as the following:
- [`cargo-component`](https://github.com/bytecodealliance/cargo-component)
- [`wac-cli`](https://github.com/bytecodealliance/wac)
- [`wkg`](https://github.com/bytecodealliance/wasm-pkg-tools)
- [`static-config`](https://github.com/componentized/static-config)

If you first install [cargo-binstall](https://github.com/cargo-bins/cargo-binstall), you can install all of the above with the following:

```sh
cargo binstall -y cargo-component
cargo binstall -y wac-cli
cargo binstall -y wkg
cargo binstall -y static-config
```

## Build

Build all of the components in this directory and compose a few more with those:

```sh
./build.sh
```

## Run

Invoke the `greeter` and `calculator` components via [`wasmtime`](https://github.com/bytecodealliance/wasmtime) (requires version 0.33 or later):

```sh
./run.sh
```

## License

Copyright (c) 2026 Modulewise Inc and the Modulewise Demos contributors.

Apache License v2.0: see [LICENSE](./LICENSE) for details.
