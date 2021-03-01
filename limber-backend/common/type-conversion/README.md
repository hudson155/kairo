# Type Conversion

Conversion services support converting between various types and strings. This is useful for both
database use cases and serialization use cases.

This library is split into two - one for the interface and one for basic implementations. This
allows other libraries to only import the interface, either to create their own implementations or
accept abstract implementations.
