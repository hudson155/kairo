package limber.gradle.plugin

import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.Project
import org.gradle.kotlin.dsl.named
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

internal typealias SourceSet = NamedDomainObjectProvider<KotlinSourceSet>

val Project.main: SourceSet get() = sourceSet("main")
val Project.test: SourceSet get() = sourceSet("test")

private fun Project.sourceSet(name: String) =
  kotlinExtension.sourceSets.named<KotlinSourceSet>(name)
