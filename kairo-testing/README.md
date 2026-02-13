# Kairo Testing

`kairo-testing` brings together a curated set of tools and defaults
that **make testing feel natural & fast**.

It's built on proven foundations.

- [JUnit](https://junit.org/): Battle-tested test runner.
- [Kotest](https://kotest.io/): Expressive matchers and assertions.
- [MockK](https://mockk.io/): Kotlin-idiomatic mocking.
- Kotlin's own coroutines testing library.

Instead of stitching these together yourself,
`kairo-testing` gives you a ready-to-go test environment with sane defaults.

```kotlin
class YourTest {
  @Test
  fun test(): Unit =
    runTest {
      doSomething(1).shouldBe("wonderful result!")
    }
}
```

## Installation

Install `kairo-testing`.

```kotlin
// build.gradle.kts

dependencies {
  testImplementation("com.highbeam.kairo:kairo-testing")
}
```

## Usage

First, configure testing in your Gradle build file.

```kotlin
// build.gradle.kts

test {
  testLogging {
    exceptionFormat = TestExceptionFormat.FULL
    events("passed", "skipped", "failed")
  }
  useJUnitPlatform()
}
```

### A note about concurrency

`kairo-testing` runs tests **concurrently by default**.
If your use case cannot support this,
create a file called `junit-platform.properties` in your project's test resources,
and set `junit.jupiter.execution.parallel.enabled` to `false`.

### A note about timeouts

`kairo-testing` uses a **default test timeout of 10 seconds**.
If your use case cannot support this, you have two options.

1. To override per test, use `runTest(timeout = 10.seconds)` instead.
2. To override globally,
   create a file called `kotest.properties` in your project's test resources,
   and set `kotlinx.coroutines.test.default_timeout` to some higher value.

### Integration tests

Integration testing for Kairo applications is handled using a different library:
[kairo-integration-testing](../kairo-integration-testing/README.md).
