package kairo.hugeString

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class HugeStringTest {
  @Test
  fun value(): Unit = runTest {
    HugeString("1").value.shouldBe("1")
  }

  @Test
  fun `equals method`(): Unit = runTest {
    HugeString("1").shouldBe(HugeString("1"))
    HugeString("1").shouldNotBe(HugeString("2"))
  }

  @Test
  fun `hashCode method`(): Unit = runTest {
    HugeString("1").hashCode().shouldBe("1".hashCode())
    HugeString("1").hashCode().shouldNotBe("2".hashCode())
  }

  @Suppress("LongLine")
  @Test
  fun `toString method`(): Unit = runTest {
    HugeString("1").toString()
      .shouldBe("HugeString(hash='c4ca4238a0b923820dcc509a6f75849b', length=1, truncated='1')")
    HugeString(List(30) { it }.joinToString("")).toString()
      .shouldBe("HugeString(hash='8d6ecd5c35a0b4c3c8b018fb6d867156', length=50, truncated='0123456789101112131415161718192021222324...')")
  }
}
