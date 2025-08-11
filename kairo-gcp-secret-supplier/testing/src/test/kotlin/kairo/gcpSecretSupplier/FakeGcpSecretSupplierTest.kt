package kairo.gcpSecretSupplier

import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kairo.protectedString.ProtectedString
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@OptIn(ProtectedString.Access::class)
internal class FakeGcpSecretSupplierTest {
  @Test
  fun `no map entry`() {
    runTest {
      val gcpSecretSupplier = FakeGcpSecretSupplier()
      gcpSecretSupplier["projects/012345678900/secrets/example/versions/1"]
        .shouldBeNull()
    }
  }

  @Test
  fun `map entry from constructor`() {
    runTest {
      val gcpSecretSupplier = FakeGcpSecretSupplier(
        mapOf(
          "projects/012345678900/secrets/example/versions/1" to ProtectedString("test_value"),
        ),
      )
      gcpSecretSupplier["projects/012345678900/secrets/example/versions/1"]
        .shouldBe(ProtectedString("test_value"))
    }
  }

  @Test
  fun `map entry from setter`() {
    runTest {
      val gcpSecretSupplier = FakeGcpSecretSupplier()
      gcpSecretSupplier["projects/012345678900/secrets/example/versions/1"] = ProtectedString("test_value")
      gcpSecretSupplier["projects/012345678900/secrets/example/versions/1"]
        .shouldBe(ProtectedString("test_value"))
    }
  }
}
