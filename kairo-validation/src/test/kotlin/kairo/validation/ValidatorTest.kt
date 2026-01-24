package kairo.validation

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class ValidatorTest {
  @Test
  fun `email address`() {
    // Valid email addresses.
    Validator.emailAddress.matches("jeff@example.com").shouldBe(true)
    Validator.emailAddress.matches("kairo-sample@airborne.software").shouldBe(true)
    Validator.emailAddress.matches("user@domain.co.uk").shouldBe(true)
    Validator.emailAddress.matches("test.name+tag@sub.domain.org").shouldBe(true)
    Validator.emailAddress.matches("a@b.co").shouldBe(true)
    Validator.emailAddress.matches("user123@test-domain.com").shouldBe(true)

    // Invalid email addresses.
    Validator.emailAddress.matches("").shouldBe(false)
    Validator.emailAddress.matches("not-an-email").shouldBe(false)
    Validator.emailAddress.matches("@domain.com").shouldBe(false)
    Validator.emailAddress.matches("user@").shouldBe(false)
    Validator.emailAddress.matches("user@.com").shouldBe(false)
    Validator.emailAddress.matches("user@@domain.com").shouldBe(false)
    Validator.emailAddress.matches("user@domain..com").shouldBe(false)
    Validator.emailAddress.matches("user name@domain.com").shouldBe(false)
  }
}
