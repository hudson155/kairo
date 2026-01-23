plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  compileOnly(project(":kairo-serialization"))

  implementation(libs.ktorHttp)

  testImplementation(project(":kairo-serialization"))
  testImplementation(project(":kairo-testing"))
}
