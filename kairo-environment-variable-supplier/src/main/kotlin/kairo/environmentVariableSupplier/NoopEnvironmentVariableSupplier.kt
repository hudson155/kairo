package kairo.environmentVariableSupplier

public object NoopEnvironmentVariableSupplier : EnvironmentVariableSupplier() {
  override fun get(name: String): Nothing {
    throw NotImplementedError("This environment variable supplier is no-op.")
  }
}
