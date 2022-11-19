@file:Suppress("ForbiddenImport")

package limber.validation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import jakarta.validation.ReportAsSingleViolation
import jakarta.validation.constraints.AssertTrue
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Constraint(validatedBy = [])
@ReportAsSingleViolation
@AssertTrue
public annotation class TrueValidator(
  val message: String = "Must be true.",
  val groups: Array<KClass<*>> = [],
  val payload: Array<KClass<out Payload>> = [],
)
