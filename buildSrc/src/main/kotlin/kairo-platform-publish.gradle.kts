plugins {
  `java-platform`
  `maven-publish`
}

publishing {
  publications {
    repositories {
      artifactRegistry()
    }
    create<MavenPublication>("bom") {
      groupId = groupId()
      artifactId = artifactId(project.path)
      from(components["javaPlatform"])
      license()
    }
  }
}

tasks.withType<PublishToMavenRepository> {
  doFirst {
    requireVersion(version)
  }
}
