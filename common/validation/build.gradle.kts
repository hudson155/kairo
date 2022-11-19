import limber.gradle.Dependencies
import limber.gradle.plugin.main

plugins {
  id("limber-jvm")
}

main {
  dependencies {
    api(Dependencies.Google.guice)
    api(Dependencies.JavaExpressionLanguage.expressly)
    api(Dependencies.Validation.hibernate)
  }
}
