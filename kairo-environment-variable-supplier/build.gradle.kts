plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  implementation(project(":bom"))

  implementation(project(":kairo-logging"))

  testImplementation(project(":kairo-logging:testing"))
  testImplementation(project(":kairo-testing"))
}

tasks.test {
  environment("KAIRO_ENVIRONMENT_VARIABLE_SUPPLIER_TEST_VARIABLE_0", "test_value")
}
