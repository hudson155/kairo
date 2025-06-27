package kairo.commandRunner

@Suppress("NotImplementedDeclaration")
public object NoopCommandRunner : CommandRunner() {
  @Insecure
  override fun run(command: String): String {
    throw NotImplementedError("This command runner is no-op.")
  }
}
