package kairo.environmentVariableSupplier

public class NoopEnvironmentVariableSupplier : EnvironmentVariableSupplier() {
  override fun get(name: String): Nothing {
    throw NotImplementedError("This environment variable supplier is no-op.")
  }
}
