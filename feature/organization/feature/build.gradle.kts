import limber.gradle.plugin.main
import limber.gradle.plugin.test

plugins {
  id("limber-jvm")
}

main {
  dependencies {
    implementation(project(":feature:organization:auth"))
    implementation(project(":feature:organization:internal-interface"))
    api(project(":feature:organization:rest-interface"))
    implementation(project(":feature:organization:rest-client"))

    api(project(":common:feature"))
    implementation(project(":feature:rest:feature"))

    implementation(project(":feature:auth0:feature"))
    implementation(project(":feature:sql:feature"))
  }
}

test {
  dependencies {
    implementation(project(":feature:auth0:testing"))
    implementation(project(":feature:rest:testing"))

    implementation(project(":feature:sql:testing"))
    implementation(project(":db:limber"))
  }
}
