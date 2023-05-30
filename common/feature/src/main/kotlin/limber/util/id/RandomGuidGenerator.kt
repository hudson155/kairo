package limber.util.id

import java.util.UUID

/**
 * This is the default/production way of generating GUIDs.
 */
public class RandomGuidGenerator : GuidGenerator {
  override fun generate(): UUID = UUID.randomUUID()
}
