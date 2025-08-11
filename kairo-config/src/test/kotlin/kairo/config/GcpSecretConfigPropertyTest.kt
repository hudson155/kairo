package kairo.config

import com.typesafe.config.ConfigFactory
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kairo.gcpSecretSupplier.FakeGcpSecretSupplier
import kairo.protectedString.ProtectedString
import kotlinx.coroutines.test.runTest
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Test

@OptIn(ProtectedString.Access::class)
internal class GcpSecretConfigPropertyTest {
  private val configManager: ConfigManager =
    ConfigManager(
      sources = listOf(
        GcpSecretConfigPropertySource(
          gcpSecretSupplier = FakeGcpSecretSupplier(
            mapOf("projects/012345678900/secrets/example/versions/1" to ProtectedString("test_value")),
          ),
        ),
      ),
    )

  @Test
  fun present(): Unit {
    val config = run {
      @Language("HOCON")
      val config = """
        type = "GcpSecret"
        id = "projects/012345678900/secrets/example/versions/1"
      """.trimIndent()
      return@run ConfigFactory.parseString(config).resolve()
    }
    runTest {
      configManager.deserializer.deserialize<ConfigPropertySource.ConfigProperty>(config)
        .let { configManager.propertyResolver.resolve(it) }
        .shouldBe("test_value")
    }
  }

  @Test
  fun missing(): Unit =
    runTest {
      val config = run {
        @Language("HOCON")
        val config = """
          type = "GcpSecret"
          id = "projects/012345678900/secrets/example/versions/2"
        """.trimIndent()
        return@run ConfigFactory.parseString(config).resolve()
      }
      configManager.deserializer.deserialize<ConfigPropertySource.ConfigProperty>(config)
        .let { configManager.propertyResolver.resolve(it) }
        .shouldBeNull()
    }
}
