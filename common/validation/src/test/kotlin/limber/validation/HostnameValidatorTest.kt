package limber.validation

import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class HostnameValidatorTest {
  @ParameterizedTest
  @ValueSource(
    strings = [
      "localhost",
      "jhudson.ca",
      "foo.bar.baz",
    ],
  )
  fun valid(value: String) {
    HostnameValidator.regex.matches(value).shouldBeTrue()
  }

  @ParameterizedTest
  @ValueSource(
    strings = [
      "",
      "foo.bar.baz/homepage",
      "http://foo.bar.baz",
    ],
  )
  fun invalid(value: String) {
    HostnameValidator.regex.matches(value).shouldBeFalse()
  }
}
