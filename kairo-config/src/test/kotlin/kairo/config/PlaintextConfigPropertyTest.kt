package kairo.config

import com.typesafe.config.ConfigFactory
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Test

internal class PlaintextConfigPropertyTest {
  private val configManager: ConfigManager = ConfigManager(
    sources = listOf(
      PlaintextConfigPropertySource(),
    ),
  )

  @Test
  fun test(): Unit =
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
