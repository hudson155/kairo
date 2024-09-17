# `kairo-testing`

This is a convenient testing library which includes some helpful test dependencies and utility code.

- [Kotest](https://kotest.io/) is provided as both the test runner and for assertions.
- [MockK](https://mockk.io/) is provided for mocking.

## Usage

### Step 1: Include the dependency

```kotlin
// build.gradle.kts

dependencies {
  testImplementation("kairo:kairo-testing:$kairoVersion")
}
```

### Step 2: Write a test

```kotlin
// src/main/kotlin/yourPackage/.../YourTest.kt

internal class YourTest {
  @Test
  fun `your test`(): Unit = runTest {
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
