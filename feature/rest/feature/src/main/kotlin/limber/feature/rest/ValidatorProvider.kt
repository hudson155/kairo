package limber.feature.rest

import com.google.inject.Inject
import com.google.inject.Provider
import jakarta.validation.Validation
import jakarta.validation.Validator
import java.time.Clock

internal class ValidatorProvider @Inject constructor(
  private val clock: Clock,
) : Provider<Validator> {
  override fun get(): Validator =
    Validation.byDefaultProvider()
      .configure()
      .clockProvider { clock }
      .buildValidatorFactory()
      .validator
}
