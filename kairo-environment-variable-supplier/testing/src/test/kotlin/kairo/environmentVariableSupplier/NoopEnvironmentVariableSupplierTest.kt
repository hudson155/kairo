package kairo.environmentVariableSupplier

import io.kotest.assertions.throwables.shouldThrow
import org.junit.jupiter.api.Test

internal class NoopEnvironmentVariableSupplierTest {
  private val environmentVariableSupplier: EnvironmentVariableSupplier = NoopEnvironmentVariableSupplier()

  @Test
  fun test() {
    shouldThrow<NotImplementedError> {
      environmentVariableSupplier["KAIRO_ENVIRONMENT_VARIABLE_SUPPLIER_TEST_VARIABLE_0"]
    }
  }
}
