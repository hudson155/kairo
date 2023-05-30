package limber.util.id

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class DeterministicIdGeneratorTest {
  private val idGenerator: DeterministicIdGenerator = DeterministicIdGenerator(prefix = "id")

  @Test
  fun test() {
    idGenerator.generate().shouldBe("id_0")
    idGenerator.generate().shouldBe("id_1")
    idGenerator.generate().shouldBe("id_2")
    idGenerator.generate().shouldBe("id_3")
    idGenerator.generate().shouldBe("id_4")
    idGenerator.reset()
    idGenerator.generate().shouldBe("id_0")
    idGenerator.generate().shouldBe("id_1")
    idGenerator.generate().shouldBe("id_2")
    idGenerator.generate().shouldBe("id_3")
    idGenerator.generate().shouldBe("id_4")

    idGenerator[0].shouldBe("id_0")
    idGenerator[1].shouldBe("id_1")
    idGenerator[2].shouldBe("id_2")
    idGenerator[3].shouldBe("id_3")
    idGenerator[4].shouldBe("id_4")
  }
}
