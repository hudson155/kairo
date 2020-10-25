dependencies {
  api(kotlin("test-junit5")) // Not used by this module - provided to consumers.
  api(Dependencies.JUnit.api) // Not used by this module - provided to consumers.
  runtimeOnly(Dependencies.JUnit.engine) // Not used by this module - provided to consumers.
  api(Dependencies.MockK.mockK) // Not used by this module - provided to consumers.
}
