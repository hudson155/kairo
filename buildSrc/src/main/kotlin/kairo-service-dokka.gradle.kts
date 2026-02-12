plugins {
  id("org.jetbrains.dokka")
}

tasks.register<Sync>("packageKdocs") {
  dependsOn(rootProject.tasks.named("dokkaGenerate"))
  from(rootProject.layout.buildDirectory.dir("dokka/html"))
  into(layout.buildDirectory.dir("resources/main/static/kdocs"))
}
