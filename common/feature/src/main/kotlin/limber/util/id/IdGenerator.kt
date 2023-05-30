package limber.util.id

public abstract class IdGenerator(protected val prefix: String) {
  public interface Factory {
    public operator fun invoke(prefix: String): IdGenerator
  }

  public abstract fun generate(): String
}
