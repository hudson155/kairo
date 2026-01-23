plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(project(":kairo-dependency-injection"))
  compileOnly(project(":kairo-feature"))
}
