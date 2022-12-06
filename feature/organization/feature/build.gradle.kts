import limber.gradle.plugin.main
import limber.gradle.plugin.test

plugins {
  id("limber-jvm")
}

main {
  dependencies {
    implementation(project(":feature:organization:auth"))
    api(project(":feature:organization:interface"))
    implementation(project(":feature:organization:client"))

    api(project(":common:feature"))
    implementation(project(":feature:rest:feature"))

    implementation(project(":feature:sql:feature"))
  }
}

test {
  dependencies {
    implementation(project(":feature:rest:testing"))

    implementation(project(":feature:sql:testing"))
    implementation(project(":db:limber"))
  }
}
