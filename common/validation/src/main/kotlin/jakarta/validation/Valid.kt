@file:Suppress("InvalidPackageDeclaration")

package jakarta.validation

/**
 * Overrides the default [Valid] annotation on the classpath,
 * with a more specific annotation target.
 */
@Target(AnnotationTarget.FIELD)
public annotation class Valid
