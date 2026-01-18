plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  implementation(project(":kairo-exception"))
  implementation(project(":kairo-testing"))
}
