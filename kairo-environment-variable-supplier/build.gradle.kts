plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  implementation(project(":kairo-logging"))

  testImplementation(project(":kairo-testing"))
}

tasks.test {
  environment("KAIRO_ENVIRONMENT_VARIABLE_SUPPLIER_TEST_VARIABLE_0", "test_value")
}
