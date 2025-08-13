package kairo.commandRunner

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@OptIn(CommandRunner.Insecure::class)
internal class FakeCommandRunnerTest {
  @Test
  fun `no map entry`(): Unit =
    runTest {
      val commandRunner = FakeCommandRunner()
      shouldThrow<NullPointerException> {
        commandRunner.run("echo \"Hello, World!\"")
      }.shouldHaveMessage("No map entry (command='echo \"Hello, World!\"').")
    }

  @Test
  fun `map entry from constructor`(): Unit =
    runTest {
      val commandRunner = FakeCommandRunner(
        mapOf(
          "echo \"Hello, World!\"" to "Hello, World!",
        ),
      )
      commandRunner.run("echo \"Hello, World!\"").readText()
        .shouldBe("Hello, World!")
    }

  @Test
  fun `map entry from setter`(): Unit =
    runTest {
      val commandRunner = FakeCommandRunner()
      commandRunner["echo \"Hello, World!\""] = "Hello, World!"
      commandRunner.run("echo \"Hello, World!\"").readText()
        .shouldBe("Hello, World!")
    }
}
