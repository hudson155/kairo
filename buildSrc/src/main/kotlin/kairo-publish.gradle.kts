plugins {
  java
  `maven-publish`
  id("com.google.cloud.artifactregistry.gradle-plugin")
}

java {
  withSourcesJar()
}

private val artifactIdRegex: Regex = Regex(":(kairo-[a-z]+(-[a-z]+)*(:[a-z]+(-[a-z]+)*)?)")

private val unspecifiedVersion: String = "unspecified"

publishing {
  publications {
    repositories {
      maven {
        url = uri("artifactregistry://us-central1-maven.pkg.dev/airborne-software/maven")
      }
    }
    create<MavenPublication>("maven") {
      groupId = "software.airborne.kairo"
      /**
       * Derives the artifact ID from the project path.
       */
      artifactId = run {
        val path = project.path
        val match = requireNotNull(artifactIdRegex.matchEntire(path)) { "Invalid project name: $path." }
        return@run match.groupValues[1].replace(':', '-')
      }
      from(components["java"])
      pom {
        licenses {
          license {
            name.set("The Apache License, Version 2.0")
            url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
            distribution.set("repo")
          }
        }
      }
    }
  }
}

private fun configurePublication(name: String, block: MavenPublication.() -> Unit) {
  gradle.taskGraph.whenReady {
    if (allTasks.any { it.name == name }) {
      publishing {
        publications.getByName<MavenPublication>("maven") {
          block()
        }
      }
    }
  }
}

configurePublication("publish") {
  require(version != unspecifiedVersion) { "Must specify a version." }
}

configurePublication("publishToMavenLocal") {
  if (version == unspecifiedVersion) {
    version = "LOCAL-SNAPSHOT"
  }
}
