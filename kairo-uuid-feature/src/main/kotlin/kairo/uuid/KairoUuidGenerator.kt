package kairo.uuid

import kotlin.uuid.Uuid

public abstract class KairoUuidGenerator {
  public abstract fun generate(): Uuid
}
