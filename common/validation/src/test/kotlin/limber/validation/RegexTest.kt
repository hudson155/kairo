package limber.validation

import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import org.junit.jupiter.api.Test

internal class RegexTest {
  @Test
  fun hostname() {
    with(Regex(Regex.Hostname.regex)) {
      matches("").shouldBeFalse()
      matches("localhost").shouldBeTrue()
      matches("jhudson.ca").shouldBeTrue()
      matches("foo.bar.baz").shouldBeTrue()
      matches("foo.bar.baz/homepage").shouldBeFalse()
      matches("http://foo.bar.baz").shouldBeFalse()
    }
  }
}
