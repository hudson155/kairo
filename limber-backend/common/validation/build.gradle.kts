plugins {
  id("limber-jvm-library")
}

dependencies {
  implementation(project(":limber-backend:common:darb"))
  testImplementation(project(":limber-backend:common:util"))
}
