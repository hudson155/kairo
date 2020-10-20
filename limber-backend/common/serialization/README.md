# `limber-backend:common:serialization`

JSON serialization is delegated to the [Jackson](https://github.com/FasterXML/jackson) library.
Creating a Limber version of Jackson's `ObjectMapper` class
is as easy as calling `limberObjectMapper()`.
