package kairo.environmentVariableSupplier

/**
 * Supplies environment variables.
 * The default implementation ([DefaultEnvironmentVariableSupplier]) delegates to Java's built-in way of doing this.
 * This abstract class is for testability.
 */
public abstract class EnvironmentVariableSupplier {
  public abstract operator fun get(name: String): String?
}
