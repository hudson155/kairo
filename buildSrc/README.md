# Build source

The following Gradle plugins help standardize the build process across Gradle modules.

- **`limber-jvm`:**
  Configures JVM Gradle modules.
  Unless and until Multiplatform modules are introduced,
  this should be used in all Gradle modules.
- **`limber-jvm-server`:**
  This plugin should be applied to Server implementations.
  It configures the JVM application,
  and uses the `ShadowJar` plugin to create a shaded (fat) JAR file
  so that the application can be run from a single file.
