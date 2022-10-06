package limber.gradle.plugin.feature

import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

interface PluginFeature {
  interface Context {
    abstract fun commonMain(target: Project): NamedDomainObjectProvider<KotlinSourceSet>
    abstract fun commonTest(target: Project): NamedDomainObjectProvider<KotlinSourceSet>
    abstract fun jvmMain(target: Project): NamedDomainObjectProvider<KotlinSourceSet>
    abstract fun jvmTest(target: Project): NamedDomainObjectProvider<KotlinSourceSet>
  }

  abstract fun configure(target: Project, context: Context)
}
