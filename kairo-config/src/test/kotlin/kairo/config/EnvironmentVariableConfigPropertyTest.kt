package kairo.config

import com.typesafe.config.ConfigFactory
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kairo.environmentVariableSupplier.FakeEnvironmentVariableSupplier
import kotlinx.coroutines.test.runTest
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Test

internal class EnvironmentVariableConfigPropertyTest {
  private val configManager: ConfigManager =
    ConfigManager(
      sources = listOf(
        EnvironmentVariableConfigPropertySource(
          environmentVariableSupplier = FakeEnvironmentVariableSupplier(
            mapOf("ENV_A" to "Hello, World!"),
          ),
        ),
      ),
    )

  @Test
  fun present(): Unit =
    runTest {
      val config = run {
        @Language("HOCON")
        val config = """
          type = "EnvironmentVariable"
          name = "ENV_A"
        """.trimIndent()
        return@run ConfigFactory.parseString(config).resolve()
      }
      configManager.deserializer.deserialize<ConfigPropertySource.ConfigProperty>(config)
        .let { configManager.propertyResolver.resolve(it) }
        .shouldBe("Hello, World!")
    }

  @Test
  fun missing(): Unit =
    runTest {
      val config = run {
        @Language("HOCON")
        val config = """
          type = "EnvironmentVariable"
          name = "ENV_B"
        """.trimIndent()
        return@run ConfigFactory.parseString(config).resolve()
      }
      configManager.deserializer.deserialize<ConfigPropertySource.ConfigProperty>(config)
        .let { configManager.propertyResolver.resolve(it) }
        .shouldBeNull()
    }
}
