# `limber-backend:common:config`

This module defines the concept of config strings.
With config strings, a config data class can have `String` properties
annotated with `@JsonDeserialize(using = ConfigStringDeserializer::class)`.
The config file(s) corresponding to that data class can either contain regular strings
or delegate the value to another source.
At this time, the only other supported source is environment variables.

### Example

```kotlin
data class MyConfig(
  @JsonDeserialize(using = ConfigStringDeserializer::class)
  val someValue: String,
)
```

**Plaintext example:**
```yaml
someValue:
  type: PLAINTEXT
  value: some non-sensitive value
```

**Environment variable example:**
```yaml
someValue:
  type: ENVIRONMENT_VARIABLE
    name: VALUE_IN_HERE
    defaultValue: whatever default
```
Note that the `defaultValue` is optional.
If it is not provided and the environment variable is not specified,
behaviour depends on the nullability of the corresponding property.
If the property is nullable, the result will be null.
If the property is not nullable, a null pointer exception will be thrown.
