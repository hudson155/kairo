dependencies {
  api(kotlin("test-junit5"))
  api(Dependencies.JUnit.api)
  runtimeOnly(Dependencies.JUnit.engine)
  api(Dependencies.MockK.mockK)
}
