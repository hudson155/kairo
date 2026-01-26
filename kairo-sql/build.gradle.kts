plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(libs.exposed)
  api(libs.exposed.crypt)
  api(libs.exposed.json)
  api(libs.exposed.kotlinDatetime)
  api(libs.exposed.money)
  api(libs.exposed.r2dbc)
}
