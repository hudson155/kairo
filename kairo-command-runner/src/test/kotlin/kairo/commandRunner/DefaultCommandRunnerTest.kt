package kairo.commandRunner

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

@OptIn(CommandRunner.Insecure::class)
internal class DefaultCommandRunnerTest {
  private val commandRunner: CommandRunner = DefaultCommandRunner

  @Test
  fun `0 lines`() {
    commandRunner.run(";").shouldBe(null)
  }

  @Test
  fun `1 line`() {
    commandRunner.run("echo \"Hello, World!\"").shouldBe("Hello, World!")
  }

  @Test
  fun `2 lines`() {
    shouldThrow<IllegalArgumentException> {
      commandRunner.run("echo -e \"First\\nSecond\"")
    }
  }
}
