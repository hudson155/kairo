package limber.util.id

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.util.UUID

internal class DeterministicGuidGeneratorTest {
  private val guidGenerator: DeterministicGuidGenerator = DeterministicGuidGenerator()

  @Test
  fun test() {
    guidGenerator.generate().shouldBe(UUID.fromString("00000000-0000-0000-abcd-000000000000"))
    guidGenerator.generate().shouldBe(UUID.fromString("00000000-0000-0000-abcd-000000000001"))
    guidGenerator.generate().shouldBe(UUID.fromString("00000000-0000-0000-abcd-000000000002"))
    guidGenerator.generate().shouldBe(UUID.fromString("00000000-0000-0000-abcd-000000000003"))
    guidGenerator.generate().shouldBe(UUID.fromString("00000000-0000-0000-abcd-000000000004"))
    guidGenerator.reset()
    guidGenerator.generate().shouldBe(UUID.fromString("00000000-0000-0000-abcd-000000000000"))
    guidGenerator.generate().shouldBe(UUID.fromString("00000000-0000-0000-abcd-000000000001"))
    guidGenerator.generate().shouldBe(UUID.fromString("00000000-0000-0000-abcd-000000000002"))
    guidGenerator.generate().shouldBe(UUID.fromString("00000000-0000-0000-abcd-000000000003"))
    guidGenerator.generate().shouldBe(UUID.fromString("00000000-0000-0000-abcd-000000000004"))

    guidGenerator[0].shouldBe(UUID.fromString("00000000-0000-0000-abcd-000000000000"))
    guidGenerator[1].shouldBe(UUID.fromString("00000000-0000-0000-abcd-000000000001"))
    guidGenerator[2].shouldBe(UUID.fromString("00000000-0000-0000-abcd-000000000002"))
    guidGenerator[3].shouldBe(UUID.fromString("00000000-0000-0000-abcd-000000000003"))
    guidGenerator[4].shouldBe(UUID.fromString("00000000-0000-0000-abcd-000000000004"))
  }
}
