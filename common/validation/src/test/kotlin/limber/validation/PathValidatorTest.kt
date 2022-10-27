package limber.validation

import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class PathValidatorTest {
  @ParameterizedTest
  @ValueSource(
    strings = [
      "",
      "/",
      "/foo",
      "/foo/",
      "/foo-_.%bar",
      "/foo/bar",
      "/foo/bar/",
    ],
  )
  fun valid(value: String) {
    PathValidator.regex.matches(value).shouldBeTrue()
  }

  @ParameterizedTest
  @ValueSource(
    strings = [
      "/foo\$bar",
    ],
  )
  fun invalid(value: String) {
    PathValidator.regex.matches(value).shouldBeFalse()
  }
}
