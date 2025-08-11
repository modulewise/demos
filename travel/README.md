# Modulewise Travel Demo

## Prerequisites

The instructions below rely on the following assumptions:

1. This [modulewise/demos](https://github.com/modulewise/demos)
repository has been cloned into the same directory as the
[modulewise/example-components](https://github.com/modulewise/example-components) repository.
2. The example-components have been built locally as described in their README.
3. The [Modulewise Toolbelt](https://github.com/modulewise/toolbelt) has
been installed locally as described in its README.

## Setup

In separate terminals:

1. run either `redis-server` or `valkey-server` (install if necessary)
2. start the Spring Boot REST API: `cd api; ./run.sh`

Generate sample data and post it to the REST API:

```sh
(
  cd api/sample-data
  python generate-flights.py
  python generate-hotels.py
  ./post-flights.sh
  ./post-hotels.sh
)
```

## Run

In a separate terminal, start the Toolbelt with the Travel Demo Tools:

```sh
toolbelt travel-demo.toml
```

Then provide the `http://localhost:3001/mcp` URL to any Agent that supports
the Streamable HTTP MCP transport and ask about flights and hotels.

## License

Copyright (c) 2025 Modulewise Inc and the Modulewise Demos contributors.

Apache License v2.0: see [LICENSE](./LICENSE) for details.
