import limber.gradle.Dependencies
import limber.gradle.main

plugins {
  id("limber-jvm")
}

main {
  dependencies {
    api(Dependencies.JavaExpressionLanguage.expressly)
    api(Dependencies.Validation.hibernate)
  }
}
