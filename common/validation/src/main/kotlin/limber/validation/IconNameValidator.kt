@file:Suppress("ForbiddenImport")

package limber.validation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import jakarta.validation.ReportAsSingleViolation
import jakarta.validation.constraints.Pattern
import kotlin.reflect.KClass

/**
 * Validates icon names from Google Fonts' Material Icons: https://fonts.google.com/icons.
 * Does this roughly, rather than comparing to a master list.
 */
@Target(AnnotationTarget.FIELD)
@Constraint(validatedBy = [])
@ReportAsSingleViolation
@Pattern(regexp = IconNameValidator.pattern)
public annotation class IconNameValidator(
  val message: String = "Must be a valid icon name ($pattern).",
  val groups: Array<KClass<*>> = [],
  val payload: Array<KClass<out Payload>> = [],
) {
  public companion object {
    internal const val pattern: String = "[a-z0-9_]{2,63}"
  }
}
