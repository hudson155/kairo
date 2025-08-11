package kairo.config

import com.typesafe.config.ConfigFactory
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Serializable
import org.junit.jupiter.api.Test

internal class ConfigLoaderTest {
  @Serializable
  internal data class Config(
    val name: String,
    val message: String,
    val description: String,
    val port: Int,
    val default: Int = 42,
    val height: Sizes,
    val width: Sizes,
    val depth: Sizes,
    val booleans: List<Boolean>,
    val other: Map<String, String>,
  ) {
    @Serializable
    data class Sizes(
      val min: Int,
      val max: Int,
      val average: Int? = null,
    )
  }

  private val configManager: ConfigManager = ConfigManager()

  @Test
  fun test(): Unit =
    runTest {
      val config = ConfigFactory.parseResources("config/testing.conf").resolve()
      configManager.deserializer.deserialize<Config>(config).shouldBe(
        Config(
          name = "My config",
          message = "Hello, World!",
          description = "First line\nSecond line\nThird line",
          port = 8080,
          default = 42,
          height = Config.Sizes(min = 2, max = 10, average = null),
          width = Config.Sizes(min = 4, max = 20, average = 16),
          depth = Config.Sizes(min = 6, max = 30, average = null),
          booleans = listOf(true, false, true),
          other = mapOf(
            "key" to "value",
            "key.2" to "value.2",
          ),
        ),
      )
    }
}
