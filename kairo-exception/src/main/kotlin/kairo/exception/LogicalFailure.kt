package kairo.exception

import kotlinx.serialization.Serializable

/**
 * "Logical failures" describe situations not deemed successful in your domain
 * but still within the realms of that domain.
 * For example, a library book not being found is a logical failure, not a real exception.
 */
public class LogicalFailure(
  public val properties: Properties,
) : Exception() {
  @Serializable
  public abstract class Properties
}

/**
 * Throws a [LogicalFailure].
 */
@Suppress("NOTHING_TO_INLINE")
public inline fun logicalFailure(properties: LogicalFailure.Properties): Nothing {
  throw LogicalFailure(properties)
}
