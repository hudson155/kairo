plugins {
  `java-platform`
  `maven-publish`
  id("com.google.cloud.artifactregistry.gradle-plugin")
}

publishing {
  publications {
    repositories {
      artifactRegistry()
    }
    create<MavenPublication>("bom") {
      groupId = groupId()
      artifactId = artifactId(project.path, Regex(":(?<artifactId>bom)"))
      from(components["javaPlatform"])
      applyLicense()
    }
  }
}
