package kairo.id

import io.kotest.matchers.shouldBe
import kairo.uuid.DeterministicKairoUuidGenerator
import kotlin.uuid.Uuid
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class DeterministicKairoUuidGeneratorTest {
  private val uuidGenerator: DeterministicKairoUuidGenerator =
    DeterministicKairoUuidGenerator()

  @Test
  fun test(): Unit = runTest {
    uuidGenerator.generate().shouldBe(Uuid.parse("00000000-0000-0000-0000-000000000000"))
    uuidGenerator.generate().shouldBe(Uuid.parse("00000000-0000-0000-0000-000000000001"))
    uuidGenerator.generate().shouldBe(Uuid.parse("00000000-0000-0000-0000-000000000002"))
    uuidGenerator.generate().shouldBe(Uuid.parse("00000000-0000-0000-0000-000000000003"))
    uuidGenerator.generate().shouldBe(Uuid.parse("00000000-0000-0000-0000-000000000004"))
    uuidGenerator.reset()
    uuidGenerator.generate().shouldBe(Uuid.parse("00000000-0000-0000-0000-000000000000"))
    uuidGenerator.generate().shouldBe(Uuid.parse("00000000-0000-0000-0000-000000000001"))
    uuidGenerator.generate().shouldBe(Uuid.parse("00000000-0000-0000-0000-000000000002"))
    uuidGenerator.generate().shouldBe(Uuid.parse("00000000-0000-0000-0000-000000000003"))
    uuidGenerator.generate().shouldBe(Uuid.parse("00000000-0000-0000-0000-000000000004"))

    uuidGenerator[4].shouldBe(Uuid.parse("00000000-0000-0000-0000-000000000004"))
    uuidGenerator[3].shouldBe(Uuid.parse("00000000-0000-0000-0000-000000000003"))
    uuidGenerator[2].shouldBe(Uuid.parse("00000000-0000-0000-0000-000000000002"))
    uuidGenerator[1].shouldBe(Uuid.parse("00000000-0000-0000-0000-000000000001"))
    uuidGenerator[0].shouldBe(Uuid.parse("00000000-0000-0000-0000-000000000000"))
  }
}
