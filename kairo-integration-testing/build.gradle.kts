plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(project(":kairo-dependency-injection"))
  api(project(":kairo-dependency-injection:testing"))
  api(project(":kairo-server"))
  compileOnly(project(":kairo-testing"))
}
