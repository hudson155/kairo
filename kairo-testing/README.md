# `kairo-testing`

This is a convenient testing library which includes some helpful test dependencies.
In the future, the intention is for this library to also include test framework and utility code.

- [Kotest](https://kotest.io/) is provided as both the test runner and for assertions.
- [MockK](https://mockk.io/) is provided for mocking.

## Usage

### Step 1: Include the dependency

```kotlin
dependencies {
  testImplementation("kairo:kairo-testing:0.3.0")
}
```

### Step 2: Write a test

```kotlin
// src/main/kotlin/yourPackage/YourTest.kt

internal class YourTest : FunSpec({
  test("test case 1") {
    shouldThrow<IllegalArgumentException> {
      doSomething("1")
    }
  }

  context("a grouping of related tests") {
    test("test case 2a") {
      doSomething("2a").shouldBe("wonderful result!")
    }
    test("test case 2b") {
      doSomething("2b").shouldBe("a different result :)")
    }
  }
})
```
