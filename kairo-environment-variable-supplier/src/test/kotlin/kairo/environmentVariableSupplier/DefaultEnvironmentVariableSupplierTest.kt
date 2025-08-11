package kairo.environmentVariableSupplier

import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class DefaultEnvironmentVariableSupplierTest {
  private val environmentVariableSupplier: EnvironmentVariableSupplier = DefaultEnvironmentVariableSupplier()

  @Test
  fun exists() =
    runTest {
      environmentVariableSupplier["KAIRO_ENVIRONMENT_VARIABLE_SUPPLIER_TEST_VARIABLE_0"]
        .shouldBe("test_value")
    }

  @Test
  fun `does not exist`() =
    runTest {
      environmentVariableSupplier["KAIRO_ENVIRONMENT_VARIABLE_SUPPLIER_TEST_VARIABLE_1"]
        .shouldBeNull()
    }
}
