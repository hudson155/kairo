# `kairo-dependency-injection`

Kairo uses [Guice](https://github.com/google/guice) for dependency injection.
There are other Kotlin-specific DI options out there
like [Koin](https://github.com/InsertKoinIO/koin) and [Kodein](https://github.com/kosi-libs/Kodein),
but Guice is quite mature and works just fine.
It also supports circular dependencies, which is useful for some architectures.

`kairo-dependency-injection` makes Guice available,
along with some utilities to make its use more idiomatic.

## Usage

This Feature is not currently intended to be a direct dependency.
