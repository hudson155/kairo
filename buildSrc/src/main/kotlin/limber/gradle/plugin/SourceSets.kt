package limber.gradle

import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.Project
import org.gradle.kotlin.dsl.named
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

val Project.main: NamedDomainObjectProvider<KotlinSourceSet> get() = sourceSet("main")
val Project.test: NamedDomainObjectProvider<KotlinSourceSet> get() = sourceSet("test")

private fun Project.sourceSet(name: String) =
  kotlinExtension.sourceSets.named<KotlinSourceSet>(name)
