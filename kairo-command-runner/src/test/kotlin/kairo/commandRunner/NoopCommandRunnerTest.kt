package kairo.commandRunner

import io.kotest.assertions.throwables.shouldThrow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@OptIn(CommandRunner.Insecure::class)
internal class NoopCommandRunnerTest {
  private val commandRunner: CommandRunner = NoopCommandRunner

  @Test
  fun `0 lines`() {
    runTest {
      shouldThrow<NotImplementedError> {
        commandRunner.run(";")
      }
    }
  }

  @Test
  fun `1 line`() {
    runTest {
      shouldThrow<NotImplementedError> {
        commandRunner.run("echo \"Hello, World!\"")
      }
    }
  }

  @Test
  fun `2 lines`() {
    runTest {
      shouldThrow<NotImplementedError> {
        commandRunner.run("echo -e \"First\\nSecond\"")
      }
    }
  }
}
