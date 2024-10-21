plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  implementation(project(":kairo-serialization"))
  api(libs.javaxMoney)
  api(libs.moneta)

  testImplementation(project(":kairo-testing"))
}
