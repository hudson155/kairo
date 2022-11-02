# Config library

Servers are configured by YAML files.
These YAML files should be placed in the `resources` folder

```
resources/
└─ config/
   ├─ development.yaml
   └─ production.yaml
```

The config for a Server will correspond with a data class extending `Config`.

## Loading configs

Use `ConfigLoader` to load configs.

```kotlin
// Will load the config/production.yaml resource.
ConfigLoader.load<MyServerConfig>("production")
```

## Special deserialization

Sometimes it's necessary for config values to come from indirect sources.

This library supports the following sources:

- **Plaintext:**
  The YAML file provides the value directly.
- **Environment variable:**
  The value is fetched from environment variables.
- **GCP secret:**
  The value is fetched from GCP Secret Manager
  using a secret ID provided in an environment variable
  (this source relies on the environment variable source).
- **Command:**
  Runs a shell command to get the value.
  _There may be unknown security concerns with this._
  At present, there's no production use case for it.
  If a use case arises, it's worth doing a thorough security evaluation of the approach.
