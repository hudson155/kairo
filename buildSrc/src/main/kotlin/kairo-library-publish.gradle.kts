plugins {
  `java-library`
  `maven-publish`
  id("com.google.cloud.artifactregistry.gradle-plugin")
}

java {
  withSourcesJar()
}

publishing {
  publications {
    repositories {
      artifactRegistry()
    }
    create<MavenPublication>("maven") {
      artifactId = artifactId(project.path, Regex(":(?<artifactId>kairo-[a-z]+(-[a-z]+)*(:[a-z]+(-[a-z]+)*)?)"))
      from(components["java"])
      applyLicense()
    }
  }
}
