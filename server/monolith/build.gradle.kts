import limber.gradle.plugin.main
import limber.gradle.plugin.test

plugins {
  id("limber-jvm")
  id("limber-jvm-server")
}

main {
  dependencies {
    implementation(project(":common:server"))

    implementation(project(":feature:google-app-engine:feature"))
    implementation(project(":feature:health-check:feature"))
    implementation(project(":feature:organization:feature"))
    implementation(project(":feature:rest:feature"))
    implementation(project(":feature:sql:feature"))

    implementation(project(":db:limber"))
  }
}

test {
  dependencies {
    implementation(project(":common:feature:testing"))
  }
}
