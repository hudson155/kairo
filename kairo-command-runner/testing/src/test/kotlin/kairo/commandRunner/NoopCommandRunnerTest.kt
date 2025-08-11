package kairo.commandRunner

import io.kotest.assertions.throwables.shouldThrow
import org.junit.jupiter.api.Test

@OptIn(CommandRunner.Insecure::class)
internal class NoopCommandRunnerTest {
  private val commandRunner: CommandRunner = NoopCommandRunner()

  @Test
  fun test() {
    shouldThrow<NotImplementedError> {
      commandRunner.run("echo \"Hello, World!\"")
    }
  }
}
