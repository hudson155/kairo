package kairo.id

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.throwable.shouldHaveMessage
import org.junit.jupiter.api.Test

internal class KairoIdTest {
  @Test
  fun prefix() {
    KairoId("library_book", "2eDS1sMt").prefix
      .shouldBe("library_book")
  }

  @Test
  fun id() {
    KairoId("library_book", "2eDS1sMt").id
      .shouldBe("2eDS1sMt")
  }

  @Test
  fun `equals method`() {
    KairoId("library_book", "2eDS1sMt")
      .shouldBe(KairoId("library_book", "2eDS1sMt"))
    KairoId("library_book", "2eDS1sMt")
      .shouldNotBe(KairoId("library_book", "X64k1rU2"))
    KairoId("library_book", "2eDS1sMt")
      .shouldNotBe(KairoId("member", "2eDS1sMt"))
  }

  @Test
  fun `hashCode method`() {
    KairoId("library_book", "2eDS1sMt").hashCode()
      .shouldBe(KairoId("library_book", "2eDS1sMt").hashCode())
    KairoId("library_book", "2eDS1sMt").hashCode()
      .shouldNotBe(KairoId("library_book", "X64k1rU2").hashCode())
    KairoId("library_book", "2eDS1sMt").hashCode()
      .shouldNotBe(KairoId("member", "2eDS1sMt").hashCode())
  }

  @Test
  fun `toString method`() {
    KairoId("library_book", "2eDS1sMt").toString()
      .shouldBe("library_book_2eDS1sMt")
  }

  @Test
  fun `fromString method, valid (typical)`() {
    KairoId.fromString("library_book_2eDS1sMt")
      .shouldBe(KairoId("library_book", "2eDS1sMt"))
  }

  @Test
  fun `fromString method, valid (atypical)`() {
    KairoId.fromString("library_bookbook")
      .shouldBe(KairoId("library", "bookbook")) // Looks odd but is valid.
  }

  @Test
  fun `fromString method, invalid (extra ID)`() {
    shouldThrow<IllegalArgumentException> {
      KairoId.fromString("library_book_2eDS1sMt_2eDS1sMt")
    }.shouldHaveMessage("Invalid Kairo ID: library_book_2eDS1sMt_2eDS1sMt.")
  }

  @Test
  fun `fromString method, invalid (kebab case)`() {
    shouldThrow<IllegalArgumentException> {
      KairoId.fromString("library-book-2eDS1sMt")
    }.shouldHaveMessage("Invalid Kairo ID: library-book-2eDS1sMt.")
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
