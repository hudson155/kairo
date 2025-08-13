plugins {
  id("kairo-platform")
  id("kairo-platform-publish")
}

dependencies {
  api(platform(libs.arrowBom))
  api(platform(libs.gcpBom))
  api(platform(libs.guavaBom))
  api(platform(libs.kotlinxCoroutinesBom))
  api(platform(libs.kotlinxSerializationBom))
  api(platform(libs.log4jBom))

  constraints {
    rootProject.subprojects.forEach { subproject ->
      if (subproject.name == project.name) return@forEach
      evaluationDependsOn(subproject.path)
      if (!subproject.plugins.hasPlugin("maven-publish")) return@forEach
      subproject.publishing.publications.withType<MavenPublication> {
        api(mapOf("group" to groupId, "name" to artifactId, "version" to version))
      }
    }
  }
}
