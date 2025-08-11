package kairo.gcpSecretSupplier

import io.kotest.assertions.throwables.shouldThrow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class NoopGcpSecretSupplierTest {
  private val gcpSecretSupplier: GcpSecretSupplier = NoopGcpSecretSupplier()

  @Test
  fun test() =
    runTest {
      shouldThrow<NotImplementedError> {
        gcpSecretSupplier["projects/012345678900/secrets/example/versions/1"]
      }
    }
}
