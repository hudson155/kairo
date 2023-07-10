import limber.gradle.plugin.main
import limber.gradle.plugin.test

plugins {
  id("limber-jvm")
}

main {
  dependencies {
    api(project(":feature:health-check:rest-interface"))
    // The health check REST Feature uses the HTTP client implementation by default.
    // Most REST Features use the Local HTTP client implementation by default.
    // This is why the client module is exposed in the API.
    api(project(":feature:health-check:rest-client"))

    api(project(":common:feature"))
    implementation(project(":feature:rest:feature"))
  }
}

test {
  dependencies {
    implementation(project(":feature:rest:testing"))
  }
}
