package kairo.buildSrc

import org.gradle.api.Plugin
import org.gradle.api.Project

public class KairoPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      applyIdeaPlugin()
      configureKotlin()
      addDefaultDependencies()
      configureTesting()
    }
  }
}
