# Serialization library

Serialization uses the [Jackson](https://github.com/FasterXML/jackson) library,
with some custom configuration.

Instances of Jackson's `ObjectMapper` created by `ObjectMapperFactory`
should be used for everything from reading configs
to deserializing request bodies and serializing response bodies.

This library supports both `JSON` and `YAML`.
