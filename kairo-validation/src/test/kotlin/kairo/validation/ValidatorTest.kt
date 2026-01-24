package kairo.validation

import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import org.junit.jupiter.api.Test

internal class ValidatorTest {
  @Test
  fun `email address`() {
    Validator.emailAddress.matches("jeff@example.com").shouldBeTrue()
    Validator.emailAddress.matches("kairo-sample@airborne.software").shouldBeTrue()
    Validator.emailAddress.matches("user@domain.co.uk").shouldBeTrue()
    Validator.emailAddress.matches("test.name+tag@sub.domain.org").shouldBeTrue()
    Validator.emailAddress.matches("a@b.co").shouldBeTrue()
    Validator.emailAddress.matches("user123@test-domain.com").shouldBeTrue()

    Validator.emailAddress.matches("").shouldBeFalse()
    Validator.emailAddress.matches("not-an-email").shouldBeFalse()
    Validator.emailAddress.matches("@domain.com").shouldBeFalse()
    Validator.emailAddress.matches("user@").shouldBeFalse()
    Validator.emailAddress.matches("user@.com").shouldBeFalse()
    Validator.emailAddress.matches("user@@domain.com").shouldBeFalse()
    Validator.emailAddress.matches("user@domain..com").shouldBeFalse()
    Validator.emailAddress.matches("user name@domain.com").shouldBeFalse()
  }
}
