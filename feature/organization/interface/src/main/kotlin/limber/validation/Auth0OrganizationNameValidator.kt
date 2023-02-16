@file:Suppress("ForbiddenImport")

package limber.validation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import jakarta.validation.ReportAsSingleViolation
import jakarta.validation.constraints.Pattern
import kotlin.reflect.KClass

/**
 * Validates Auth0 organization names.
 */
@Target(AnnotationTarget.FIELD)
@Constraint(validatedBy = [])
@ReportAsSingleViolation
@Pattern(regexp = Auth0OrganizationNameValidator.pattern)
public annotation class Auth0OrganizationNameValidator(
  val message: String = "Must be a valid Auth0 organization name ($pattern).",
  val groups: Array<KClass<*>> = [],
  val payload: Array<KClass<out Payload>> = [],
) {
  public companion object {
    internal const val pattern: String = "(?=.{3,50}$)[a-z0-9](-?[a-z0-9])*"
  }
}
