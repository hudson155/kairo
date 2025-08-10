package kairo.commandRunner

public object NoopCommandRunner : CommandRunner() {
  @Insecure
  override fun run(command: String): Nothing {
    throw NotImplementedError("This command runner is no-op.")
  }
}
