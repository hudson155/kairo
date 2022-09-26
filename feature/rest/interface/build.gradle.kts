plugins {
  id("limber-jvm")
}

dependencies {
  api(project(":common:serialization:interface"))
  api(Dependencies.JavaExpressionLanguage.expressly)
  api(Dependencies.Ktor.httpJvm)
  api(Dependencies.Validation.hibernate)
}
