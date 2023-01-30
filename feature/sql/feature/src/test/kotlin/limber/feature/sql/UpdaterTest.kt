package limber.feature.sql

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.util.Optional

internal class UpdaterTest {
  @Test
  fun `update with explicit`() {
    fun update(existing: String, new: String?): String =
      limber.feature.sql.update(existing = existing, new = new)

    update("existing", null).shouldBe("existing")
    update("existing", "new").shouldBe("new")
  }

  @Test
  fun `update with optional, originally null`() {
    fun update(existing: String?, new: Optional<String>?): String? =
      limber.feature.sql.update(existing = existing, new = new)

    update(null, null).shouldBe(null)
    update(null, Optional.empty()).shouldBe(null)
    update(null, Optional.of("new")).shouldBe("new")
  }

  @Test
  fun `update with optional, originally not null`() {
    fun update(existing: String?, new: Optional<String>?): String? =
      limber.feature.sql.update(existing = existing, new = new)

    update("existing", null).shouldBe("existing")
    update("existing", Optional.empty()).shouldBe(null)
    update("existing", Optional.of("new")).shouldBe("new")
  }
}
