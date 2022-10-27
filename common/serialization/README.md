# Serialization library

Serialization uses the [Jackson](https://github.com/FasterXML/jackson) library,
with some custom configuration.

Instances of Jackson's `ObjectMapper` created by `ObjectMapperFactory`
should be used for everything from reading configs
to deserializing request bodies and serializing response bodies.

This library supports both `JSON` and `YAML`.

## Interface

Sometimes we need to add serialization annotations,
but don't want to include the entire serialization library.
In this case, we can use the nested interface Gradle module.

## Customizations

- `TrimWhitespace`:
  String properties have their starting and ending whitespace trimmed.
  This behaviour can be customized using this annotation.
