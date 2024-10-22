plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  api(libs.javaxMoney)
  api(libs.moneta)

  testImplementation(project(":kairo-testing"))
}
