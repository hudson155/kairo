private fun isPublish(): Boolean =
  gradle.startParameter.taskNames.any { it == "publish" }

allprojects {
  group = "software.airborne.kairo"
  version = providers
    .gradleProperty("version")
    .orElse(if (isPublish()) throw IllegalArgumentException("Must specify a version.") else "LOCAL-SNAPSHOT")
    .get()
}
