package kairo.environmentVariableSupplier

import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class FakeEnvironmentVariableSupplierTest {
  @Test
  fun `no map entry`(): Unit =
    runTest {
      val environmentVariableSupplier = FakeEnvironmentVariableSupplier()
      environmentVariableSupplier["KAIRO_ENVIRONMENT_VARIABLE_SUPPLIER_TEST_VARIABLE_0"]
        .shouldBeNull()
    }

  @Test
  fun `map entry from constructor`(): Unit =
    runTest {
      val environmentVariableSupplier = FakeEnvironmentVariableSupplier(
        mapOf(
          "KAIRO_ENVIRONMENT_VARIABLE_SUPPLIER_TEST_VARIABLE_0" to "test_value",
        ),
      )
      environmentVariableSupplier["KAIRO_ENVIRONMENT_VARIABLE_SUPPLIER_TEST_VARIABLE_0"]
        .shouldBe("test_value")
    }

  @Test
  fun `map entry from setter`(): Unit =
    runTest {
      val environmentVariableSupplier = FakeEnvironmentVariableSupplier()
      environmentVariableSupplier["KAIRO_ENVIRONMENT_VARIABLE_SUPPLIER_TEST_VARIABLE_0"] = "test_value"
      environmentVariableSupplier["KAIRO_ENVIRONMENT_VARIABLE_SUPPLIER_TEST_VARIABLE_0"]
        .shouldBe("test_value")
    }
}
