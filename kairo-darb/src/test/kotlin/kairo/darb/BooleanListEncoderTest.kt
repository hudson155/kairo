package kairo.darb

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldBeEmpty
import io.kotest.matchers.throwable.shouldHaveMessage
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class BooleanListEncoderTest {
  @Test
  fun encode(): Unit = runTest {
    BooleanListEncoder.encode(emptyList()).shouldBeEmpty()
    BooleanListEncoder.encode(listOf(false)).shouldBe("0")
    BooleanListEncoder.encode(listOf(true)).shouldBe("1")
    BooleanListEncoder.encode(listOf(false, false, false, false)).shouldBe("0000")
    BooleanListEncoder.encode(listOf(false, false, false, true)).shouldBe("0001")
    BooleanListEncoder.encode(listOf(true, false, false, false)).shouldBe("1000")
    BooleanListEncoder.encode(listOf(true, true, true, true)).shouldBe("1111")
    BooleanListEncoder.encode(listOf(false, false, false, false, false)).shouldBe("00000")
    BooleanListEncoder.encode(listOf(false, false, false, false, true)).shouldBe("00001")
    BooleanListEncoder.encode(listOf(true, false, false, false, false)).shouldBe("10000")
    BooleanListEncoder.encode(listOf(true, true, true, true, true)).shouldBe("11111")
  }

  @Test
  fun decode(): Unit = runTest {
    BooleanListEncoder.decode("").shouldBeEmpty()
    BooleanListEncoder.decode("0").shouldBe(listOf(false))
    BooleanListEncoder.decode("1").shouldBe(listOf(true))
    BooleanListEncoder.decode("0000").shouldBe(listOf(false, false, false, false))
    BooleanListEncoder.decode("0001").shouldBe(listOf(false, false, false, true))
    BooleanListEncoder.decode("1000").shouldBe(listOf(true, false, false, false))
    BooleanListEncoder.decode("1111").shouldBe(listOf(true, true, true, true))
    BooleanListEncoder.decode("00000").shouldBe(listOf(false, false, false, false, false))
    BooleanListEncoder.decode("00001").shouldBe(listOf(false, false, false, false, true))
    BooleanListEncoder.decode("10000").shouldBe(listOf(true, false, false, false, false))
    BooleanListEncoder.decode("11111").shouldBe(listOf(true, true, true, true, true))

    shouldThrow<IllegalArgumentException> { BooleanListEncoder.decode("2") }
      .shouldHaveMessage("Bit strings must only consist of 0s and 1s.")
  }
}
