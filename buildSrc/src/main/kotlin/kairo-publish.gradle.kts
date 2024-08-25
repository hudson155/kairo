plugins {
  `maven-publish`
  id("com.google.cloud.artifactregistry.gradle-plugin")
}

publishing {
  publications {
    repositories {
      maven {
        url = uri("artifactregistry://us-central1-maven.pkg.dev/kairo-13/kairo-13")
      }
    }
    create<MavenPublication>("maven") {
      groupId = "kairo"
      artifactId = project.name
      version = "0.5.0"
      from(components["java"])
    }
  }
}

gradle.taskGraph.whenReady {
  if (allTasks.any { it.name == "publishToMavenLocal" }) {
    publishing.publications.getByName<MavenPublication>("maven") {
      version += "-SNAPSHOT"
    }
  }
}
