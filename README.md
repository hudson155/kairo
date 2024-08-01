# Kairo

Kairo is an application framework built for Kotlin.

## Project information

### Major dependencies

- Gradle 8.9
- Kotlin 2.0.0
- Java 21
- Guice 7.0.0

## Modules

- [kairo-darb](kairo-darb/):
  Home of `DarbEncoder`, which encodes a list of booleans into a Dense-ish Albeit Readable Binary (DARB) string.
- [kairo-dependency-injection](kairo-dependency-injection/):
  Makes Guice available,
  along with some utilities to make its use more idiomatic.
- [kairo-feature](kairo-feature/):
  Features are the primary building block of Kairo applications.

## Brand guidelines

- Treat Kairo _Features_ as a proper noun (the first letter should be capitalized).
