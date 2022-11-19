package limber.validation

import com.google.inject.Inject
import com.google.inject.Provider
import jakarta.validation.Validation
import jakarta.validation.Validator
import org.hibernate.validator.HibernateValidator
import java.time.Clock

public class ValidatorProvider @Inject constructor(
  private val clock: Clock,
) : Provider<Validator> {
  override fun get(): Validator =
    Validation.byProvider(HibernateValidator::class.java)
      .configure()
      .addValueExtractor(OptionalValueExtractor())
      .clockProvider { clock }
      .buildValidatorFactory()
      .validator
}
