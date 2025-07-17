package kairo.commandRunner

import io.kotest.assertions.throwables.shouldThrow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@OptIn(CommandRunner.Insecure::class)
internal class NoopCommandRunnerTest {
  private val commandRunner: CommandRunner = NoopCommandRunner

  @Test
  fun `0 lines`(): Unit = runTest {
    shouldThrow<NotImplementedError> {
      commandRunner.run(";")
    }
  }

  @Test
  fun `1 line`(): Unit = runTest {
    shouldThrow<NotImplementedError> {
      commandRunner.run("echo \"Hello, World!\"")
    }
  }

  @Test
  fun `2 lines`(): Unit = runTest {
    shouldThrow<NotImplementedError> {
      commandRunner.run("echo -e \"First\\nSecond\"")
    }
  }
}
