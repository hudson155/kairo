import kairo.buildSrc.main

plugins {
  id("kairo")
  id("kairo-logging")
  id("kairo-publish")
}

main {
  dependencies {
    api(project(":kairo-feature"))
  }
}
