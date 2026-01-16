# Hello World Demo

## Prerequisites

The instructions below rely on the following assumptions:

1. This [modulewise/demos](https://github.com/modulewise/demos) repository has been cloned, and these instructions will be followed from within the `hello-world` directory.
2. The Wasm Components have been built locally as described in [components/README.md](./../components/README.md).
3. The [Modulewise Toolbelt](https://github.com/modulewise/toolbelt) has been installed locally as described in its README.

## Run the Toolbelt MCP Server

In a separate terminal, start the Toolbelt with the Hello World tools:

```sh
toolbelt hello-world.toml
```

You can then use any MCP client or MCP-enabled Agent that supports the Streamable HTTP transport by connecting to `http://localhost:3001/mcp`.

## Calling the Tools

A simple way to try out these tools is to use scripts in this repository that provde a barebones MCP client via `curl` commands.

Change into the `scripts/mcp` directory:

```sh
cd ../scripts/mcp
```

Initialize an MCP session:

```sh
./initialize.sh
```

If successful, you will see the protocol version and server info in a response.

With the session established, you can list available tools:

```sh
./list_tools.sh
```

You should see three tools:
- `hello.greeter.greet`
- `aloha.greeter.greet`
- `pirate.greeter.greet`

First, call the `hello.greeter.greet` tool with a `name` parameter:

```sh
./call_tool.sh hello.greeter.greet name=World
```

You will see the greeting in the response:

```json
{
  "jsonrpc": "2.0",
  "id": 3,
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

If you take a look at the `hello-world.toml` file passed to the `toolbelt` server above, you will see that the `aloha` variant uses the same `greeter.wasm` but includes `config.greeting = "Aloha"` to override the default "Hello" greeting:

```sh
./call_tool.sh aloha.greeter.greet name=World
```

And indeed, it responds with that greeting:

```json
{
  "jsonrpc": "2.0",
  "id": 4,
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

Finally, notice in `hello-world.toml` that the `pirate` variant is an interceptor.
It adds a prefix and suffix, but otherwise composes with the `aloha` component.

```toml
[pirate]
uri = "../components/lib/intercepting-greeter.wasm"
config.prefix = "Ahoy and "
config.suffix = " Arrr!"
intercepts = ["aloha"]
exposed = true
```

Call it just like the others:

```sh
./call_tool.sh pirate.greeter.greet name=World
```

And see the result of the composed interception:

```json
{
  "jsonrpc": "2.0",
  "id": 5,
  "result": {
    "content": [
      {
        "type": "text",
        "text": "Ahoy and Aloha World! Arrr!"
      }
    ],
    "isError": false
  }
}
```

If you want to explore the Wasm Components, have a look at the greeter [project](./../components/greeter) and its WebAssembly Interface Type (WIT) [definition](./../components/wit/greeter.wit) as well as the
[intercepting-greeter](./../components/intercepting-greeter) and its
[WIT](./../components/wit/intercepting-greeter.wit).

## License

Copyright (c) 2026 Modulewise Inc and the Modulewise Demos contributors.

Apache License v2.0: see [LICENSE](./LICENSE) for details.
