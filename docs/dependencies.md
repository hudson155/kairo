# Dependencies

These instructions are for upgrading dependencies.

## CI Ubuntu

- Files in the [GitHub workflows directory](/.github/workflows).

Upgrade them all at the same time, then ensure CI passes.

## Detekt

- The [buildSrc build file](/buildSrc/build.gradle.kts).
- [Versions.kt](/buildSrc/src/main/kotlin/limber/gradle/Versions.kt).

Upgrade them all at the same time,
then follow the instructions in [.detekt/config.yml](/.detekt/config.yml)
to update to the new rule set.
Ensure `./gradlew check` runs Detekt and passes.

## GCP Cloud SQL proxy

- Deployment files in the [Kubernetes Server directory](/infrastructure/kubernetes/config/server).

## GitHub Actions

- Files in the [GitHub Actions directory](/.github/actions).
- Files in the [GitHub workflows directory](/.github/workflows).

Upgrade each action then ensure CI passes.

## Gradle

- The [Gradle wrapper properties file](/gradle/wrapper/gradle-wrapper.properties).

This file should not be modified directly.
Gradle should be upgraded using the `./gradlew wrapper` command.
Don't commit the `.bat` file (delete it).

## Java

- The [Java setup GitHub Action](/.github/actions/set-up-java/action.yaml).
- [KotlinFeature](/buildSrc/src/main/kotlin/limber/gradle/plugin/feature/KotlinFeature.kt).
- The [Dockerfile](/server/Dockerfile).
- IntelliJ's [misc.xml file](/.idea/misc.xml).

References to Java 8 or Java 1.8 should not be upgraded,
since they refer to the bytecode version.

## JavaScript dependencies

- The [package.json file](/web/package.json).

Upgrade major versions one at a time.

## JVM dependencies

- [Versions.kt](/buildSrc/src/main/kotlin/limber/gradle/Versions.kt).

Upgrade minor versions together.
Upgrade major versions one at a time.

## Kotlin

- The [buildSrc build file](/buildSrc/build.gradle.kts).
- IntelliJ's [kotlinc.xml file](/.idea/kotlinc.xml).

## Kubernetes

- GCP.

This should upgrade automatically.

## Node.js

- The [.nvmrc file](/web/.nvmrc).
- The [package.json file](/web/package.json).

## Postgres

- [GCP through Terraform](/infrastructure/terraform/sql.tf).
- The [CI GitHub workflow](/.github/workflows/ci.yaml).
- [Versions.kt](/buildSrc/src/main/kotlin/limber/gradle/Versions.kt).

## Shadow

- The [buildSrc build file](/buildSrc/build.gradle.kts).

## Terraform providers

- The [main Terraform file](/infrastructure/terraform/main.tf).

All providers should be upgraded.
