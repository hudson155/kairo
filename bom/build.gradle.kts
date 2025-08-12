plugins {
  `java-platform`
  publishing
}

javaPlatform {
  allowDependencies()
}

dependencies {
  api(platform(libs.gcpBom))
  api(platform(libs.guavaBom))
  api(platform(libs.kotlinxCoroutinesBom))
  api(platform(libs.kotlinxSerializationBom))
  api(platform(libs.log4jBom))

  constraints {
    rootProject.subprojects.forEach { subproject ->
      if (subproject.name == project.name) return@forEach
      if (!subproject.plugins.hasPlugin("maven-publish")) return@forEach
      evaluationDependsOn(subproject.path)
      subproject.publishing.publications {
        this as MavenPublication
        api(mapOf("group" to groupId, "name" to artifactId, "version" to version))
      }
    }
  }
}
