package kairo.id

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.throwable.shouldHaveMessage
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class KairoIdTest {
  @Test
  fun prefix(): Unit = runTest {
    KairoId("library_book", "2eDS1sMt").prefix
      .shouldBe("library_book")
  }

  @Test
  fun id(): Unit = runTest {
    KairoId("library_book", "2eDS1sMt").id
      .shouldBe("2eDS1sMt")
  }

  @Test
  fun `equals method`(): Unit = runTest {
    KairoId("library_book", "2eDS1sMt")
      .shouldBe(KairoId("library_book", "2eDS1sMt"))
    KairoId("library_book", "2eDS1sMt")
      .shouldNotBe(KairoId("library_book", "X64k1rU2"))
    KairoId("library_book", "2eDS1sMt")
      .shouldNotBe(KairoId("member", "2eDS1sMt"))
  }

  @Test
  fun `hashCode method`(): Unit = runTest {
    KairoId("library_book", "2eDS1sMt").hashCode()
      .shouldBe(KairoId("library_book", "2eDS1sMt").hashCode())
    KairoId("library_book", "2eDS1sMt").hashCode()
      .shouldNotBe(KairoId("library_book", "X64k1rU2").hashCode())
    KairoId("library_book", "2eDS1sMt").hashCode()
      .shouldNotBe(KairoId("member", "2eDS1sMt").hashCode())
  }

  @Test
  fun `toString method`(): Unit = runTest {
    "${KairoId("library_book", "2eDS1sMt")}"
      .shouldBe("library_book_2eDS1sMt")
  }

  @Test
  fun `fromString method, valid (typical)`(): Unit = runTest {
    KairoId.fromString("library_book_2eDS1sMt")
      .shouldBe(KairoId("library_book", "2eDS1sMt"))
  }

  @Test
  fun `fromString method, valid (atypical)`(): Unit = runTest {
    KairoId.fromString("library_bookbook")
      .shouldBe(KairoId("library", "bookbook")) // Looks odd but is valid.
  }

  @Test
  fun `fromString method, invalid (extra ID)`(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      KairoId.fromString("library_book_2eDS1sMt_2eDS1sMt")
    }.shouldHaveMessage("Invalid Kairo ID: library_book_2eDS1sMt_2eDS1sMt.")
  }

  @Test
  fun `fromString method, invalid (kebab case)`(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      KairoId.fromString("library-book-2eDS1sMt")
    }.shouldHaveMessage("Invalid Kairo ID: library-book-2eDS1sMt.")
  }

  @Test
  fun `fromString method, invalid (capital letter)`(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      KairoId.fromString("Library_book_2eDS1sMt")
    }.shouldHaveMessage("Invalid Kairo ID: Library_book_2eDS1sMt.")
  }

  @Test
  fun `fromString method, invalid (invalid character)`(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      KairoId.fromString("library_böok_2eDS1sMt")
    }.shouldHaveMessage("Invalid Kairo ID: library_böok_2eDS1sMt.")
  }
}
