package kairo.config

import com.typesafe.config.ConfigFactory
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Test

internal class ConfigPropertyTest {
  private val configManager: ConfigManager = ConfigManager()

  @Test
  fun `environment variable`(): Unit =
    runTest {
      val config = run {
        @Language("HOCON")
        val config = """
          type = "EnvironmentVariable"
          name = "KAIRO_CONFIG_SOURCE_TEST"
        """.trimIndent()
        return@run ConfigFactory.parseString(config).resolve()
      }
      configManager.deserializer.deserialize<ConfigPropertySource.ConfigProperty>(config)
        .let { configManager.propertyResolver.resolve(it) }
        .shouldBe("Hello, World!")
    }

  @Test
  fun plaintext(): Unit =
    runTest {
      val config = run {
        @Language("HOCON")
        val config = """
          type = "Plaintext"
          value = "Hello, World!"
        """.trimIndent()
        return@run ConfigFactory.parseString(config).resolve()
      }
      configManager.deserializer.deserialize<ConfigPropertySource.ConfigProperty>(config)
        .let { configManager.propertyResolver.resolve(it) }
        .shouldBe("Hello, World!")
    }
}
