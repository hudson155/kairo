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
- [kairo-logging](kairo-logging/):
  Logging uses the [kotlin-logging](https://github.com/oshai/kotlin-logging) interface,
  which should be configured to use Apache Log4j 2 under the hood.
- [kairo-server](kairo-server/):
  A Server is an application that runs a set of Features.

## Brand guidelines

- Treat Kairo _Features_ and _Servers_ as proper noun (the first letter should be capitalized).
