plugins {
  id("org.jetbrains.dokka")
}

repositories {
  mavenCentral()
}

dokka {
  moduleName.set("Kairo")
}

dependencies {
  /**
   * Automatically include all subprojects that have the Dokka plugin applied.
   */
  rootProject.subprojects.forEach { subproject ->
    evaluationDependsOn(subproject.path)
    if (subproject.plugins.hasPlugin("org.jetbrains.dokka")) {
      dokka(project(subproject.path))
    }
  }
}
