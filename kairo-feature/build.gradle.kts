import kairo.buildSrc.main

plugins {
  id("kairo")
  id("kairo-publish")
}

main {
  dependencies {
    api(project(":kairo-dependency-injection"))
  }
}
