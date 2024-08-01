import kairo.buildSrc.Dependencies
import kairo.buildSrc.main

plugins {
  id("kairo")
  id("kairo-publish")
}

main {
  dependencies {
    api(Dependencies.kotlinLoggingJvm)
  }
}
