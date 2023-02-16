@file:Suppress("ForbiddenImport")

package limber.validation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import jakarta.validation.ReportAsSingleViolation
import jakarta.validation.constraints.Pattern
import kotlin.reflect.KClass

/**
 * Intentionally doesn't use the normal path validator.
 * Feature paths are stricter (not all paths are allowed).
 */
@Target(AnnotationTarget.FIELD)
@Constraint(validatedBy = [])
@ReportAsSingleViolation
@Pattern(regexp = FeaturePathValidator.pattern)
public annotation class FeaturePathValidator(
  val message: String = "Must be a valid feature path ($pattern).",
  val groups: Array<KClass<*>> = [],
  val payload: Array<KClass<out Payload>> = [],
) {
  public companion object {
    internal const val pattern: String = "/(?=.{3,31}\$)[A-Za-z0-9](-?[A-Za-z0-9])*"
  }
}
