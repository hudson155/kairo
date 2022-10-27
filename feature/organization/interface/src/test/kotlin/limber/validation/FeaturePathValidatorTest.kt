package limber.validation

import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class FeaturePathValidatorTest {
  @ParameterizedTest
  @ValueSource(
    strings = [
      "/foo",
      "/foo-bar",
      "/foo/bar",
    ],
  )
  fun valid(value: String) {
    FeaturePathValidator.regex.matches(value).shouldBeTrue()
  }

  @ParameterizedTest
  @ValueSource(
    strings = [
      "",
      "/",
      "/-foo",
      "/foo-",
      "/foo/",
      "/foo/bar/",
    ],
  )
  fun invalid(value: String) {
    FeaturePathValidator.regex.matches(value).shouldBeFalse()
  }
}
