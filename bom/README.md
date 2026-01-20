# Kairo BOM

The Bill of Materials (BOM) keeps all Kairo library versions aligned.

When using multiple Kairo libraries,
the BOM ensures you don't have to manually specify versions for each one,
and prevents version conflicts between them.

## Installation

```kotlin
// build.gradle.kts

dependencies {
  implementation(platform("software.airborne.kairo:bom:6.0.0"))

  // Now you can add Kairo libraries without specifying versions.
  implementation("software.airborne.kairo:kairo-logging")
  implementation("software.airborne.kairo:kairo-serialization")
}
```

## When to use the full BOM

If you're building your entire application with Kairo,
consider using the [full BOM](../bom-full/README.md) instead.
It aligns not only Kairo libraries but also the external dependencies Kairo uses.
