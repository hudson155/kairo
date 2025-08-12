plugins {
  `java-library`
  `maven-publish`
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
      groupId = groupId()
      artifactId = artifactId(project.path)
      from(components["java"])
      license()
    }
  }
}

tasks.withType<PublishToMavenRepository> {
  requireVersion(version)
}
