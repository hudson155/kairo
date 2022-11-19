@file:Suppress("ForbiddenImport")

package limber.validation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import jakarta.validation.ReportAsSingleViolation
import jakarta.validation.constraints.Pattern
import kotlin.reflect.KClass

/**
 * Validates organization names.
 * Organization names are currently limited to 31 characters
 * so that they show up fine in the top navigation bar.
 */
@Target(AnnotationTarget.FIELD)
@Constraint(validatedBy = [])
@ReportAsSingleViolation
@Pattern(regexp = OrganizationNameValidator.pattern)
public annotation class OrganizationNameValidator(
  val message: String = "Must be a valid organization name.",
  val groups: Array<KClass<*>> = [],
  val payload: Array<KClass<out Payload>> = [],
) {
  public companion object {
    internal const val pattern: String = "$CHAR{3,31}"
  }
}
