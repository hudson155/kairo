plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  api(kotlin("reflect"))

  implementation(libs.geantyref)

  testImplementation(project(":kairo-testing"))
}
