package kairo.config

import com.typesafe.config.ConfigFactory
import io.kotest.matchers.shouldBe
import kairo.commandRunner.FakeCommandRunner
import kairo.protectedString.ProtectedString
import kotlinx.coroutines.test.runTest
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Test

@OptIn(ProtectedString.Access::class)
internal class CommandConfigPropertyTest {
  private val configManager: ConfigManager =
    ConfigManager(
      sources = listOf(
        CommandConfigPropertySource(
          commandRunner = FakeCommandRunner(
            mapOf("gcloud auth print-access-token" to "abcdefg"),
          ),
        ),
      ),
    )

  @Test
  fun test(): Unit =
    runTest {
      val config = run {
        @Language("HOCON")
        val config = """
          type = "Command"
          command = "gcloud auth print-access-token"
        """.trimIndent()
        return@run ConfigFactory.parseString(config).resolve()
      }
      configManager.deserialize<ConfigProperty>(config)
        .let { configManager.resolve(it) }
        .shouldBe("abcdefg")
    }
}
