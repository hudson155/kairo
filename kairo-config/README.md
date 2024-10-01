# `kairo-config`

Home of `ConfigLoader`, which loads configs for Kairo Servers from YAML files.
Includes support for config extension and application,
as well as various sources.

- **Config extension:** Configs can extend other configs
  by specifying `extends: other-config-name` as a top-level YAML property.
- **Config application:** Configs can apply other configs
  by specifying `apply: [other-config-name-0, other-config-name-1]` as a top-level YAML property

If no config name is provided to `ConfigLoader`,
it will use the `KAIRO_CONFIG` environment variable to identify the config name.

### Source support

Config files won't typically contain _all_ the config data,
because some has to come from the environment or from sensitive sources.
When deserializing some types,
a few different sources can be used.

- `Command`:
  This will run a shell command to get the value.
  ```yaml
  message:
    source: "Command"
    command: "echo \"Hello, World!\""
  ```
- `EnvironmentVariable`:
  This will retrieve the value from the referenced environment variable.
  ```yaml
  message:
    source: "EnvironmentVariable"
    name: "MESSAGE"
    default: "Default value." # The default value is optional.
  ```
- `GcpSecret`:
  This will retrieve the value from the referenced GCP secret.
  ```yaml
  message:
    source: "GcpSecret"
    id: "projects/012345678900/secrets/example/versions/1"
  ```

### Source compatibility

Each type is compatible with certain sources according to the table below.

|                   | `Command` | `EnvironmentVariable` | `GcpSecret` |
|-------------------|-----------|-----------------------|-------------|
| `Int`             | Insecure  | Secure                | Insecure    |
| `ProtectedString` | Insecure  | Insecure              | Secure      |
| `String`          | Insecure  | Secure                | Insecure    |

## Usage

### Step 1: Include the dependency

```kotlin
// build.gradle.kts

dependencies {
  api("kairo:kairo-config:$kairoVersion")
}
```

### Step 2: Try reading a basic config

Your YAML file **must** be in `config/*`.

```kotlin
// src/main/kotlin/yourPackage/server/monolith/MonolithServerConfig.kt

data class MonolithServerConfig(
  val message: String,
)
```

```yaml
# src/main/resources/config/basic-config.yaml

message: "Hello, World!"
```

```kotlin
// src/main/kotlin/yourPackage/server/monolith/MonolithServer.kt

ConfigLoader.createDefault().load<MonolithServerConfig>("basic-config")
```

### Step 3: Try reading a complex config

This is a "complex" config in the sense that it utilizes both _config extension_ and _config application_.
The large number of config properties is needed to demonstrate by example how these features work.

```kotlin
// src/main/kotlin/yourPackage/server/monolith/MonolithServerConfig.kt

data class MonolithServerConfig(
  val message: String,
  val name: String,
  val port: Int,
  val height: Sizes,
  val width: Sizes,
  val depth: Sizes,
) {
  data class Sizes(
    val min: Int,
    val max: Int,
    val average: Int,
  )
}
```

```yaml
# src/main/resources/config/base-config.yaml

extraRootProperty: "This breaks the config."
name: "Base config"
port: 8080
height: { min: 2, max: 9, other: 9 }
width: { min: 3, max: 19, average: 15 }
```

```yaml
# src/main/resources/config/applied-config-0.yaml

extraRootProperty: { remove: { } }
message: "Applied config 0"
name: { remove: { } }
height:
  merge: { max: 10, average: 8, other: { remove: { } } }
depth:
  replace: { min: 6, max: 30, average: 24 }
```

```yaml
# src/main/resources/config/config-with-extension-and-application.yaml

extends: base-config

apply:
  - applied-config-0

message: "Hello, World!"
name: "My config"
port: 8080
width:
  replace: { min: 4, max: 20, average: 16 }

```

```kotlin
// src/main/kotlin/yourPackage/server/monolith/MonolithServer.kt

ConfigLoader.createDefault().load<MonolithServerConfig>("config-with-extension-and-application")
```
