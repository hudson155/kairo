import limber.gradle.plugin.main

plugins {
  id("limber-jvm")
}

main {
  dependencies {
    api(project(":feature:form:interface"))
    api(project(":feature:rest:client"))
  }
}
