package limber.validation

import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class IconNameValidatorTest {
  @ParameterizedTest
  @ValueSource(
    strings = [
      "4k",
      "keyboard_double_arrow_down",
    ],
  )
  fun valid(value: String) {
    IconNameValidator.regex.matches(value).shouldBeTrue()
  }

  @ParameterizedTest
  @ValueSource(
    strings = [
      "",
      "4",
      "k",
      "4K",
      "Keyboard_double_arrow_down",
      "keyboard double arrow down",
    ],
  )
  fun invalid(value: String) {
    IconNameValidator.regex.matches(value).shouldBeFalse()
  }
}
