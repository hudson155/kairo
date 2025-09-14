plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(libs.exposed.core)
  api(libs.exposed.crypt)
  api(libs.exposed.jdbc)
  api(libs.exposed.json)
  api(libs.exposed.kotlinDatetime)
  api(libs.exposed.money)
}
