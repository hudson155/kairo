package kairo.commandRunner

import java.io.BufferedReader
import java.io.StringReader

public class FakeCommandRunner(
  map: Map<String, String> = emptyMap(),
) : CommandRunner() {
  private val map: MutableMap<String, String> = map.toMutableMap()

  @Insecure
  override fun run(command: String): BufferedReader {
    val result = map[command] ?: throw NullPointerException("No map entry for command: $command.")
    return BufferedReader(StringReader(result))
  }

  public operator fun set(command: String, value: String) {
    map[command] = value
  }
}
