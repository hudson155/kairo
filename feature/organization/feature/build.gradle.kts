import limber.gradle.main
import limber.gradle.test

plugins {
  id("limber-jvm")
}

main {
  dependencies {
    api(project(":feature:organization:interface"))
    implementation(project(":feature:organization:client"))

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
