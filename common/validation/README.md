# Validation library

Limber uses [Hibernate Validator](https://hibernate.org/validator/) for validation.
_Note: This is unrelated to Hibernate's ORM product._

## Hibernate annotations

In order to validate the data classes that represent JSON reps,
use Hibernate Validator annotations.
However, due to Kotlin's syntax **they must be prefixed by `@field:`**.
For example, instead of `@Size(min = 3, max = 255)`
we must do `@field:Size(min = 3, max = 255)`.

## Validators

Many input strings are validated using Regex.
Unfortunately, due to Hibernate's API requiring compile-time constants,
using an abstract class is not possible.
Instead, validators simply follow a consistent pattern

```kotlin
public object MyValidator {
  public const val message: String = "must be xyz"
  public const val pattern: String = "regex pattern"
  public val regex: Regex = Regex(pattern)
}
```

Validators should be unit tested.

Validators that are common across the app live in this Gradle module,
but feature-specific validators live in the corresponding feature.
