package kairo.buildSrc

import org.gradle.api.Plugin
import org.gradle.api.Project

public class KairoLoggingPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      addLoggingDependencies()
    }
  }
}
