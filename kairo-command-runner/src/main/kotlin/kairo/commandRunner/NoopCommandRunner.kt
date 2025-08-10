package kairo.commandRunner

public object NoopCommandRunner : CommandRunner() {
  @Insecure
  override fun run(command: String): Sequence<String> {
    throw NotImplementedError("This command runner is no-op.")
  }
}
