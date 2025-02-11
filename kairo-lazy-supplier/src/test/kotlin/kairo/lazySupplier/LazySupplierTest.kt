package kairo.lazySupplier

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class LazySupplierTest {
  @Test
  fun value(): Unit = runTest {
    var count = 0
    val supplier = LazySupplier { ++count }
    count.shouldBe(0)
    supplier.get().shouldBe(1)
    count.shouldBe(1)
    supplier.get().shouldBe(1)
    count.shouldBe(1)
  }
}
