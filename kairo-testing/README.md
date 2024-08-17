# `kairo-testing`

This is a convenient testing library which includes some helpful test dependencies.
In the future, the intention is for this library to also include test framework and utility code.

- [Kotest](https://kotest.io/) is provided as both the test runner and for assertions.
- [MockK](https://mockk.io/) is provided for mocking.

## Usage

### Step 1: Include the dependency

```kotlin
// build.gradle.kts

dependencies {
  testImplementation("kairo:kairo-testing:0.3.0")
}
```

### Step 2: Write a test

```kotlin
// src/main/kotlin/yourPackage/.../YourTest.kt

internal class YourTest {
  @Test
  fun `test case 1`() {
    shouldThrow<IllegalArgumentException> {
      doSomething("1")
    }
  }

  @Test
  fun `test case 2`() {
    doSomething("2").shouldBe("wonderful result!")
  }
}
```
