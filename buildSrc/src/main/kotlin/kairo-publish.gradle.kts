plugins {
  java
  `maven-publish`
  id("com.google.cloud.artifactregistry.gradle-plugin")
}

java {
  withSourcesJar()
}

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
        val regex = Regex(":([a-z]+(-[a-z]+)*(:[a-z]+(-[a-z]+)*)?)")
        val path = project.path
        val match = requireNotNull(regex.matchEntire(path)) { "Invalid project name: $path." }
        return@run match.groupValues[1].replace(':', '-')
      }
      version = "5.15.1"
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

gradle.taskGraph.whenReady {
  if (allTasks.any { it.name == "publishToMavenLocal" }) {
    publishing {
      publications.getByName<MavenPublication>("maven") {
        version += "-SNAPSHOT"
      }
    }
  }
}
