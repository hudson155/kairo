package kairo.environmentVariableSupplier

@Suppress("NotImplementedDeclaration")
public object NoopEnvironmentVariableSupplier : EnvironmentVariableSupplier() {
  public override fun get(name: String, default: String?): String? {
    throw NotImplementedError("This environment variable supplier is no-op.")
  }
}
