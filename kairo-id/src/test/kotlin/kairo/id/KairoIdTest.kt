package kairo.id

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.throwable.shouldHaveMessage
import org.junit.jupiter.api.Test

internal class KairoIdTest {
  @Test
  fun prefix() {
    KairoId("library_book", "0").prefix.shouldBe("library_book")
  }

  @Test
  fun id() {
    KairoId("library_book", "0").id.shouldBe("0")
  }

  @Test
  fun `equals method`() {
    KairoId("library_book", "0").shouldBe(KairoId("library_book", "0"))
    KairoId("library_book", "0").shouldNotBe(KairoId("library_book", "1"))
    KairoId("library_book", "0").shouldNotBe(KairoId("member", "0"))
  }

  @Test
  fun `hashCode method`() {
    KairoId("library_book", "0").hashCode().shouldBe(KairoId("library_book", "0").hashCode())
    KairoId("library_book", "0").hashCode().shouldNotBe(KairoId("library_book", "1").hashCode())
    KairoId("library_book", "0").hashCode().shouldNotBe(KairoId("member", "0").hashCode())
  }

  @Test
  fun `toString method`() {
    KairoId("library_book", "0").toString().shouldBe("library_book_0")
  }

  @Test
  fun `fromString method, valid (typical)`() {
    KairoId.fromString("library_book_0").shouldBe(KairoId("library_book", "0"))
  }

  @Test
  fun `fromString method, valid (atypical)`() {
    KairoId.fromString("library_book").shouldBe(KairoId("library", "book")) // Looks odd but is valid.
  }

  @Test
  fun `fromString method, invalid (extra number)`() {
    shouldThrow<IllegalArgumentException> {
      KairoId.fromString("library_book_0_0")
    }.shouldHaveMessage("Invalid Kairo ID: library_book_0_0.")
  }

  @Test
  fun `fromString method, invalid (kebab case)`() {
    shouldThrow<IllegalArgumentException> {
      KairoId.fromString("library-book-0")
    }.shouldHaveMessage("Invalid Kairo ID: library-book-0.")
  }

  @Test
  fun `fromString method, invalid (capital letter)`() {
    shouldThrow<IllegalArgumentException> {
      KairoId.fromString("Library_book_0")
    }.shouldHaveMessage("Invalid Kairo ID: Library_book_0.")
  }

  @Test
  fun `fromString method, invalid (invalid character)`() {
    shouldThrow<IllegalArgumentException> {
      KairoId.fromString("library_böok_0")
    }.shouldHaveMessage("Invalid Kairo ID: library_böok_0.")
  }
}
