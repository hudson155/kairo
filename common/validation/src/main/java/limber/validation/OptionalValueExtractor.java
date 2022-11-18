package limber.validation;

import jakarta.validation.valueextraction.ExtractedValue;
import jakarta.validation.valueextraction.UnwrapByDefault;
import jakarta.validation.valueextraction.ValueExtractor;

import java.util.Optional;

/**
 * By default, Hibernate supports Optional validation by annotating property type parameters.
 * However, we want to annotate the property directly, partially for consistency,
 * but also partially due to annotation target differences between Java and Kotlin.
 *
 * We achieve this behaviour and enable this syntax using [@UnwrapByDefault],
 * which is disabled by the default [OptionalValueExtractor].
 *
 * This class is written in Java
 * because it requires annotating [Optional]'s type argument in the [ValueExtractor] constructor,
 * which is not something Kotlin supports.
 */
@UnwrapByDefault
class OptionalValueExtractor implements ValueExtractor<Optional<@ExtractedValue ?>> {
  @Override
  public void extractValues(Optional<?> originalValue, ValueExtractor.ValueReceiver receiver) {
    receiver.value(null, originalValue.orElse(null));
  }
}
