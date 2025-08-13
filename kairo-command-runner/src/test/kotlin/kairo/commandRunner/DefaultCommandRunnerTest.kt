package kairo.commandRunner

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@OptIn(CommandRunner.Insecure::class)
internal class DefaultCommandRunnerTest {
  private val commandRunner: CommandRunner = DefaultCommandRunner()

  @Test
  fun `0 lines`(): Unit =
    runTest {
      commandRunner.run(";").readLines()
        .shouldBeEmpty()
    }

  @Test
  fun `1 line`(): Unit =
    runTest {
      commandRunner.run("echo \"Hello, World!\"").readLines()
        .shouldContainExactly("Hello, World!")
    }

  @Test
  fun `2 lines`(): Unit =
    runTest {
      commandRunner.run("echo -e \"First\\nSecond\"").readLines()
        .shouldContainExactly("First", "Second")
    }
}
