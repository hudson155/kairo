package kairo.commandRunner

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@OptIn(CommandRunner.Insecure::class)
internal class DefaultCommandRunnerTest {
  private val commandRunner: CommandRunner = DefaultCommandRunner

  @Test
  fun `0 lines`() {
    runTest {
      commandRunner.run(";").readLines()
        .shouldBeEmpty()
    }
  }

  @Test
  fun `1 line`() {
    runTest {
      commandRunner.run("echo \"Hello, World!\"").readLines()
        .shouldContainExactly("Hello, World!")
    }
  }

  @Test
  fun `2 lines`() {
    runTest {
      commandRunner.run("echo -e \"First\\nSecond\"").readLines()
        .shouldContainExactly("First", "Second")
    }
  }
}
