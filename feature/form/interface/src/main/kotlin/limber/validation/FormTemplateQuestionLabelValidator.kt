@file:Suppress("ForbiddenImport")

package limber.validation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import jakarta.validation.ReportAsSingleViolation
import jakarta.validation.constraints.Pattern
import kotlin.reflect.KClass

/**
 * Validates form template question labels.
 * Form template question labels are arbitrarily limited to 63 characters.
 */
@Target(AnnotationTarget.FIELD)
@Constraint(validatedBy = [])
@ReportAsSingleViolation
@Pattern(regexp = FormTemplateQuestionLabelValidator.pattern)
public annotation class FormTemplateQuestionLabelValidator(
  val message: String = "Must be a valid question label ($pattern).",
  val groups: Array<KClass<*>> = [],
  val payload: Array<KClass<out Payload>> = [],
) {
  public companion object {
    internal const val pattern: String = "$CHAR{3,63}"
  }
}
