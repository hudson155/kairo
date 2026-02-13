# Kairo Validation

Common validation patterns.

## Installation

Install `kairo-validation`.

```kotlin
// build.gradle.kts

dependencies {
  implementation("com.highbeam.kairo:kairo-validation")
}
```

## Usage

### Email address validation

The `Validator.emailAddress` regex follows the
[WHATWG HTML Standard](https://html.spec.whatwg.org/multipage/input.html#valid-e-mail-address).

```kotlin
Validator.emailAddress.matches("jeff@example.com") // true
Validator.emailAddress.matches("not-an-email") // false
```
