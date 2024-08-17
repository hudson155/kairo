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
    publications {
      create<MavenPublication>("maven") {
        groupId = "kairo"
        artifactId = project.name
        version = "0.4.0"
        from(components["java"])
      }
    }
  }
}
