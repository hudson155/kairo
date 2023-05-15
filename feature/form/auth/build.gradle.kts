import limber.gradle.plugin.main
import limber.gradle.plugin.test

plugins {
  id("limber-jvm")
}

main {
  dependencies {
    api(project(":feature:rest:feature"))
  }
}

test {
  dependencies {
    api(project(":feature:rest:testing"))
  }
}
