plugins {
  id("kairo-platform")
  id("kairo-platform-publish")
}

dependencies {
  /**
   * Automatically include all other modules.
   */
  constraints {
    rootProject.subprojects.forEach { subproject ->
      if (subproject.name == project.name) return@forEach
      evaluationDependsOn(subproject.path)
      if (!subproject.plugins.hasPlugin("maven-publish")) return@forEach
      subproject.publishing.publications.withType<MavenPublication> {
        api("$groupId:$artifactId:$version")
      }
    }
  }
}
