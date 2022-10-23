package limber.util.guid

import java.util.UUID

/**
 * The [GuidGenerator] should be used to generate GUIDs, rather than using [UUID.randomUUID].
 * This helps make testing a lot easier.
 */
public interface GuidGenerator {
  public fun generate(): UUID
}
