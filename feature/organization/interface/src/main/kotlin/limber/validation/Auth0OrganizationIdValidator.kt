@file:Suppress("ForbiddenImport")

package limber.validation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import jakarta.validation.ReportAsSingleViolation
import jakarta.validation.constraints.Pattern
import kotlin.reflect.KClass

/**
 * Validates Auth0 organization IDs.
 * There's no documented standard for these IDs.
 * If Auth0 changes their syntax in the future, this validator will need to be updated
 * in a backwards-compatible way.
 */
@Target(AnnotationTarget.FIELD)
@Constraint(validatedBy = [])
@ReportAsSingleViolation
@Pattern(regexp = Auth0OrganizationIdValidator.pattern)
public annotation class Auth0OrganizationIdValidator(
  val message: String = "Must be a valid Auth0 organization ID ($pattern).",
  val groups: Array<KClass<*>> = [],
  val payload: Array<KClass<out Payload>> = [],
) {
  public companion object {
    internal const val pattern: String = "org_[A-Za-z0-9]{16}"
  }
}
