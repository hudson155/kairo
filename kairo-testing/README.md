# Kairo Testing

This is basically an amalgam of libraries that forms the recommended way to test Kairo applications.
It's based on:

- [JUnit](https://junit.org/)
- [Kotest](https://kotest.io/)
- [MockK](https://mockk.io/) for mocking
- Kotlin's own coroutines testing library

```kotlin
class YourTest {
  @Test
  fun test(): Unit =
    runTest {
      doSomething("1").shouldBe("wonderful result!")
    }
}
```

## Installation

Get started by installing `kairo-testing`.

```kotlin
// build.gradle.kts

dependencies {
  implementation("software.airborne.kairo:kairo-testing:6.0.0")
}
```

## Usage

First, configure testing.

```kotlin
test {
  testLogging {
    events("passed", "skipped", "failed")
    showStandardStreams = true
  }
  useJUnitPlatform()
}
```

> **IMPORTANT NOTE:**
> `kairo-testing` runs tests concurrently by default.
> If your use case cannot support this,
> create a file called `junit-platform.properties` in your project's test resources,
> and set `junit.jupiter.execution.parallel.enabled` to `false`.

> **ANOTHER IMPORTANT NOTE:**
> `kairo-testing` uses a default test timeout of 10 seconds by default.
> If your use case cannot support this, you have two options.
>
> To override per test, use `runTest(timeout = 10.seconds)` instead.
>
> To override globally,
> create a file called `kotest.properties` in your project's test resources,
> and set `kotlinx.coroutines.test.default_timeout` to some higher value.

### Simple tests

Simple tests look like this.

```kotlin
class YourTest {
  @Test
  fun test(): Unit =
    runTest {
      doSomething("1").shouldBe("wonderful result!")
    }
}
```

### Complex tests

More complex tests can use the `setup`, `precondition`, `test` and `postcondition` indicators.
These indicators are completely optional, providing no functionality.
Their purpose is strictly to make test more readable.

```kotlin
class YourTest {
  @Test
  fun test(): Unit =
    runTest {
      setup("Your setup") {
        // ...
      }

      precondition("Your precondition") {
        // ...
      }

      test("Your test") {
        shouldThrow<IllegalArgumentException> {
          doSomething("0")
        }
        doSomething("1").shouldBe("wonderful result!")
      }

      postcondition("Your postcondition") {
        // ...
      }
    }
}
```

### Mocking

For Mocking, see [MockK](https://mockk.io/)'s documentation.
