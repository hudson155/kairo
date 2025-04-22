# Chores

## Update all dependencies

Update dependencies in the following locations.
Review the release notes for any relevant changes.

**Major dependencies:**

- **Gradle:** [gradle-wrapper.properties](../gradle/wrapper/gradle-wrapper.properties).
  Use the command `./gradlew wrapper --gradle-version=<version>` to upgrade.
- **Kotlin:** [buildSrc/build.gradle.kts](../buildSrc/build.gradle.kts).
- **Java:** [kairo.gradle.kts](../buildSrc/src/main/kotlin/kairo.gradle.kts).
  The Java version must correspond with the Kotlin version.
  If it doesn't, you'll get a compilation error.
- **Detekt:** [buildSrc/build.gradle.kts](../buildSrc/build.gradle.kts).
  When upgrading Detekt versions, review `default-detekt-config.yaml` as well as the changelog.
  Make corresponding changes as necessary in [the config file](../.detekt/config.yaml).
- **Jackson:** [libs.versions.toml](../gradle/libs.versions.toml).
  Kairo's integration with Jackson is very tight. Be careful when upgrading versions.
  Search for any new or modified usages of `JsonMappingException`s
  and ensure these are handled by `ExceptionManager`.
- **Ktor:** [libs.versions.toml](../gradle/libs.versions.toml).
  Kairo's integration with Ktor is very tight. Be careful when upgrading versions.
  Search for any new or modified usages of exceptions from `io.ktor.server.plugins.ErrorsKt`
  and ensure these are handled by `ExceptionManager`.

**Other dependencies:**

- Other dependencies specified in [buildSrc/build.gradle.kts](../buildSrc/build.gradle.kts).
- Other dependencies specified in [libs.versions.toml](../gradle/libs.versions.toml).

## Delete stale GitHub branches

There probably aren't a lot, but it's nice to delete them every once in a while.
