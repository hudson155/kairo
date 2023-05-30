package limber.util.id

/**
 * This is a deterministic way of generating IDs that's useful for tests.
 */
public class DeterministicIdGenerator(prefix: String) : IdGenerator(prefix) {
  public class Factory : IdGenerator.Factory {
    override fun invoke(prefix: String): DeterministicIdGenerator =
      DeterministicIdGenerator(prefix)
  }

  private var seed: Int = 0

  public fun reset() {
    seed = 0
  }

  override fun generate(): String = get(seed).also { seed++ }

  public operator fun get(i: Int): String = "${prefix}_$i"
}
