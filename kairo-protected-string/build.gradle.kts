plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  implementation(project(":kairo-serialization"))
  implementation(project(":kairo-util"))

  testImplementation(project(":testing"))
}
