@file:Suppress("ForbiddenImport")

package limber.validation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import jakarta.validation.ReportAsSingleViolation
import jakarta.validation.constraints.Pattern
import kotlin.reflect.KClass

/**
 * Validates a hostname.
 * It should be more-or-less correct, but feel free to make adjustments if required.
 */
@Target(AnnotationTarget.FIELD)
@Constraint(validatedBy = [])
@ReportAsSingleViolation
@Pattern(regexp = HostnameValidator.pattern)
public annotation class HostnameValidator(
  val message: String = "Must be a valid hostname ($pattern).",
  val groups: Array<KClass<*>> = [],
  val payload: Array<KClass<out Payload>> = [],
) {
  public companion object {
    internal const val pattern: String = "$HOSTNAME_PORTION?(\\.$HOSTNAME_PORTION?)*"
  }
}
