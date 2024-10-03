package kairo.commandRunner

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@OptIn(CommandRunner.Insecure::class)
internal class DefaultCommandRunnerTest {
  private val commandRunner: CommandRunner = DefaultCommandRunner

  @Test
  fun `0 lines`(): Unit = runTest {
    commandRunner.run(";").shouldBe(null)
  }

  @Test
  fun `1 line`(): Unit = runTest {
    commandRunner.run("echo \"Hello, World!\"").shouldBe("Hello, World!")
  }

  @Test
  fun `2 lines`(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      commandRunner.run("echo -e \"First\\nSecond\"")
    }
  }
}
