package kairo.id

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class DeterministicKairoIdGeneratorTest {
  private val idGenerator: DeterministicKairoIdGenerator =
    DeterministicKairoIdGenerator(prefix = "library_book", length = 2)

  @Test
  fun test() {
    idGenerator.generate().shouldBe(KairoId("library_book", "00"))
    idGenerator.generate().shouldBe(KairoId("library_book", "01"))
    idGenerator.generate().shouldBe(KairoId("library_book", "02"))
    idGenerator.generate().shouldBe(KairoId("library_book", "03"))
    idGenerator.generate().shouldBe(KairoId("library_book", "04"))
    idGenerator.reset()
    idGenerator.generate().shouldBe(KairoId("library_book", "00"))
    idGenerator.generate().shouldBe(KairoId("library_book", "01"))
    idGenerator.generate().shouldBe(KairoId("library_book", "02"))
    idGenerator.generate().shouldBe(KairoId("library_book", "03"))
    idGenerator.generate().shouldBe(KairoId("library_book", "04"))

    idGenerator[0].shouldBe(KairoId("library_book", "00"))
    idGenerator[1].shouldBe(KairoId("library_book", "01"))
    idGenerator[2].shouldBe(KairoId("library_book", "02"))
    idGenerator[3].shouldBe(KairoId("library_book", "03"))
    idGenerator[4].shouldBe(KairoId("library_book", "04"))
  }
}
