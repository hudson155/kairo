package kairo.uuid

import kotlin.uuid.Uuid

/**
 * This is the default/production way of generating UUIDs.
 */
public class RandomKairoUuidGenerator : KairoUuidGenerator() {
  override fun generate(): Uuid =
    Uuid.random()
}
