package kairo.buildSrc

import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.Project
import org.gradle.kotlin.dsl.named
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

internal typealias SourceSet = NamedDomainObjectProvider<KotlinSourceSet>

public val Project.main: SourceSet
  get() = sourceSet("main")
public val Project.test: SourceSet
  get() = sourceSet("test")

private fun Project.sourceSet(name: String): SourceSet =
  kotlinExtension.sourceSets.named<KotlinSourceSet>(name)
