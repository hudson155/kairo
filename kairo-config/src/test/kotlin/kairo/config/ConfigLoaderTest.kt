package kairo.config

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.every
import io.mockk.mockk
import kairo.commandRunner.CommandRunner
import kairo.commandRunner.DefaultCommandRunner
import kairo.environmentVariableSupplier.EnvironmentVariableSupplier
import kairo.gcpSecretSupplier.GcpSecretSupplier
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class ConfigLoaderTest {
  internal data class TestConfig(
    val message: String,
    val name: String,
    val port: Int,
    val height: Sizes,
    val width: Sizes,
    val depth: Sizes,
  ) {
    internal data class Sizes(
      val min: Int,
      val max: Int,
      val average: Int,
    )
  }

  private val commandRunner: CommandRunner = DefaultCommandRunner
  private val environmentVariableSupplier: EnvironmentVariableSupplier =
    mockk {
      every { get("KAIRO_ALLOW_INSECURE_CONFIG_SOURCES", any()) } returns null
    }
  private val gcpSecretSupplier: GcpSecretSupplier = mockk()

  private val configLoader: ConfigLoader =
    ConfigLoader(
      ConfigLoaderConfig(
        commandRunner = commandRunner,
        environmentVariableSupplier = environmentVariableSupplier,
        gcpSecretSupplier = gcpSecretSupplier,
      ),
    )

  private val testConfig: TestConfig =
    TestConfig(
      message = "Hello, World!",
      name = "My config",
      port = 8080,
      height = TestConfig.Sizes(min = 2, max = 10, average = 8),
      width = TestConfig.Sizes(min = 4, max = 20, average = 16),
      depth = TestConfig.Sizes(min = 6, max = 30, average = 24),
    )

  @Test
  fun `environment variable not set`(): Unit = runTest {
    every { environmentVariableSupplier["KAIRO_CONFIG", any()] } returns null
    shouldThrow<IllegalStateException> {
      configLoader.load<TestConfig>()
    }.shouldHaveMessage("Config name was not provided and KAIRO_CONFIG is not set.")
  }

  @Test
  fun `basic config from environment variable`(): Unit = runTest {
    every { environmentVariableSupplier["KAIRO_CONFIG", any()] } returns "basic-config"
    configLoader.load<TestConfig>().shouldBe(testConfig)
  }

  @Test
  fun `basic config`(): Unit = runTest {
    configLoader.load<TestConfig>("basic-config").shouldBe(testConfig)
  }

  @Test
  fun `config with extra root property`(): Unit = runTest {
    configLoadingShouldFail {
      configLoader.load<TestConfig>("config-with-extra-root-property")
    }
  }

  @Test
  fun `config with extra nested property`(): Unit = runTest {
    configLoadingShouldFail {
      configLoader.load<TestConfig>("config-with-extra-nested-property")
    }
  }

  @Test
  fun `config with missing root property`(): Unit = runTest {
    configLoadingShouldFail {
      configLoader.load<TestConfig>("config-with-missing-root-property")
    }
  }

  @Test
  fun `config with missing nested property`(): Unit = runTest {
    configLoadingShouldFail {
      configLoader.load<TestConfig>("config-with-missing-nested-property")
    }
  }

  @Test
  fun `config with trivial extension`(): Unit = runTest {
    configLoader.load<TestConfig>("config-with-trivial-extension").shouldBe(testConfig)
  }

  @Test
  fun `config with extension`(): Unit = runTest {
    configLoader.load<TestConfig>("config-with-extension").shouldBe(testConfig)
  }

  @Test
  fun `config with application`(): Unit = runTest {
    configLoader.load<TestConfig>("config-with-application").shouldBe(testConfig)
  }

  @Test
  fun `config with extension and application`(): Unit = runTest {
    configLoader.load<TestConfig>("config-with-application").shouldBe(testConfig)
  }
}
