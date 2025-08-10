package kairo.commandRunner

import java.io.BufferedReader

/**
 * Runs shell commands.
 * The default implementation ([DefaultCommandRunner]) delegates to Java's built-in way of doing this.
 * This abstract class is for testability.
 *
 * This is NOT CONSIDERED SECURE, and should not be used in production unless additional measures are in place.
 * To prevent insecure usage, opting into [Insecure] is necessary.
 */
public abstract class CommandRunner {
  @RequiresOptIn
  @Target(AnnotationTarget.FUNCTION)
  public annotation class Insecure

  /**
   * Runs the command and returns the result.
   * Call [BufferedReader.readLines] to get the full result,
   * or use [BufferedReader.useLines] to safely read a sequence of lines if the output is very large.
   */
  @Insecure
  public abstract fun run(command: String): BufferedReader
}
