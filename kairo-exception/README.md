# Kairo Exceptions (Logical Failures)

**"Logical failures" describe situations not deemed successful in your domain
but still within the realms of that domain.**
For example, a user record not being found is a logical failure, not a "real exception".

Different programming languages & frameworks offer various ways to handle logical failures.

- Java, Go, Python, and others use exceptions (Java has checked exceptions).
- Kotlin and Rust both have `Result`.
- Scala and others have typed results like `Either`, `Option`, or `Maybe`.

In Kotlin, libraries like [Arrow](https://arrow-kt.io/learn/typed-errors/working-with-typed-errors/)
offer non-exception approaches similar to Scala using constructs like `Raise` and `Either`.
While this solves the real problem of how to handle logical failures,
these constructs aren't native to the language nor widespread across the ecosystem.
They result in extra boilerplate & verbosity —
a lot of "glue" code is necessary, especially when interacting with external libraries.
They also cause significant cognitive overhead and have a high learning curve.

`kairo-exception` embraces JVM exceptions,
but **still differentiates logical failures from real exceptions**
using the `LogicalFailure` exception subclass.

### Other benefits

- Kairo's logical failures can be **serialized to JSON**,
  roughly conforming to RFC 9457
  and giving clients (such as frontend apps) get **rich error information**.
- Kairo's logical failures are easily testable
  thanks to some helpful Kotest utils.

## Installation

Install `kairo-exception`.\
You can also install `kairo-exception-testing` for testing.

```kotlin
// build.gradle.kts

dependencies {
  implementation("software.airborne.kairo:kairo-exception")
  testImplementation("software.airborne.kairo:kairo-exception-testing")
}
```

## Usage

Extend `LogicalFailure` to create your own logical failures.

```kotlin
data class UserNotFound(
  val userId: UserId?,
) : LogicalFailure() {
  override val type: String = "UserNotFound"
  override val status: HttpStatusCode = HttpStatusCode.NotFound
  override val title: String = "User not found"

  override fun JsonObjectBuilder.buildJson() {
    put("userId", Json.encodeToJsonElement(libraryBookId))
  }
}

// Serializes to { "type": "UserNotFound", "status": 404, "title": "User not found", "detail": null, "userId": "..." }
```
