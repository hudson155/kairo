plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  api(kotlin("reflect"))

  implementation(project(":kairo-util"))

  implementation(libs.geantyref)

  testImplementation(project(":testing"))
}
