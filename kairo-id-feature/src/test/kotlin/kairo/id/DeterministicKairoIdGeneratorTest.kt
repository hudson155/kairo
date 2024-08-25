package kairo.id

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class DeterministicKairoIdGeneratorTest {
  private val idGenerator: DeterministicKairoIdGenerator =
    DeterministicKairoIdGenerator(prefix = "library_book")

  @Test
  fun test(): Unit = runTest {
    idGenerator.generate().shouldBe(KairoId("library_book", "00000000"))
    idGenerator.generate().shouldBe(KairoId("library_book", "00000001"))
    idGenerator.generate().shouldBe(KairoId("library_book", "00000002"))
    idGenerator.generate().shouldBe(KairoId("library_book", "00000003"))
    idGenerator.generate().shouldBe(KairoId("library_book", "00000004"))
    idGenerator.reset()
    idGenerator.generate().shouldBe(KairoId("library_book", "00000000"))
    idGenerator.generate().shouldBe(KairoId("library_book", "00000001"))
    idGenerator.generate().shouldBe(KairoId("library_book", "00000002"))
    idGenerator.generate().shouldBe(KairoId("library_book", "00000003"))
    idGenerator.generate().shouldBe(KairoId("library_book", "00000004"))

    idGenerator[4].shouldBe(KairoId("library_book", "00000004"))
    idGenerator[3].shouldBe(KairoId("library_book", "00000003"))
    idGenerator[2].shouldBe(KairoId("library_book", "00000002"))
    idGenerator[1].shouldBe(KairoId("library_book", "00000001"))
    idGenerator[0].shouldBe(KairoId("library_book", "00000000"))
  }
}
