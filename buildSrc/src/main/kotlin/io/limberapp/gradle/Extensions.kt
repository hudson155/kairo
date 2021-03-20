package io.limberapp.gradle

import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Project
import org.gradle.api.plugins.JavaApplication
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal val Project.application: JavaApplication
  get() = project.extensions.getByName("application") as JavaApplication

internal fun Project.application(action: JavaApplication.() -> Unit) {
  project.extensions.configure("application", action)
}

internal fun Project.detekt(action: DetektExtension.() -> Unit) {
  project.extensions.configure("detekt", action)
}

internal fun Project.kotlin(action: KotlinMultiplatformExtension.() -> Unit) {
  project.extensions.configure("kotlin", action)
}
