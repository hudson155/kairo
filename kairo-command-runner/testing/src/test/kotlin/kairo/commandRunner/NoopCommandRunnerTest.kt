package kairo.commandRunner

import io.kotest.assertions.throwables.shouldThrow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@OptIn(CommandRunner.Insecure::class)
internal class NoopCommandRunnerTest {
  private val commandRunner: CommandRunner = NoopCommandRunner()

  @Test
  fun test(): Unit =
    runTest {
      shouldThrow<NotImplementedError> {
        commandRunner.run("echo \"Hello, World!\"")
      }
    }
}
