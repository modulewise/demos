# Hello World Demo

## Prerequisites

1. Clone the [modulewise/toolbelt](https://github.com/modulewise/toolbelt) repo,
and install locally as described in its README.
2. Clone the [modulewise/example-components](https://github.com/modulewise/example-components) repo
into the same directory as this modulewise/demos repo, and follow the build instructions in its README. 

## Run

In a separate terminal:

```sh
toolbelt hello-world.toml
```

Then initialize an MCP session:

```sh
./../scripts/mcp/initialize.sh
```

If successful, you will see the protocol version and server info in a response.

With the session established, you can call the `hello_greet` tool with a `name` parameter:

```sh
./../scripts/mcp/call_tool.sh hello_greet name=World
```

You will see the greeting in the response:

```json
{
  "jsonrpc": "2.0",
  "id": 2,
  "result": {
    "content": [
      {
        "type": "text",
        "text": "Hello World!"
      }
    ],
    "isError": false
  }
}
```

The `hello-world.toml` file also includes an `aloha` variant that
uses the same `greeter.wasm` but includes `config.greeting = "Aloha"`
to override the default "Hello" greeting:

```sh
./../scripts/mcp/call_tool.sh aloha_greet name=World
```

And it responds with a different greeting:

```json
{
  "jsonrpc": "2.0",
  "id": 3,
  "result": {
    "content": [
      {
        "type": "text",
        "text": "Aloha World!"
      }
    ],
    "isError": false
  }
}
```

## License

Copyright (c) 2025 Modulewise Inc and the Modulewise Demos contributors.

Apache License v2.0: see [LICENSE](./LICENSE) for details.
