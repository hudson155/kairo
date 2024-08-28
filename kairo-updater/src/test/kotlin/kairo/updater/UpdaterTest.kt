package kairo.updater

import io.kotest.matchers.shouldBe
import java.util.Optional
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class UpdaterTest {
  @Test
  fun `update with explicit`(): Unit = runTest {
    fun update(existing: String, new: String?): String =
      kairo.updater.update(existing = existing, new = new)

    update("existing", null).shouldBe("existing")
    update("existing", "new").shouldBe("new")
  }

  @Test
  fun `update with optional, originally null`(): Unit = runTest {
    fun update(existing: String?, new: Optional<String>?): String? =
      kairo.updater.update(existing = existing, new = new)

    update(null, null).shouldBe(null)
    update(null, Optional.empty()).shouldBe(null)
    update(null, Optional.of("new")).shouldBe("new")
  }

  @Test
  fun `update with optional, originally non-null`(): Unit = runTest {
    fun update(existing: String?, new: Optional<String>?): String? =
      kairo.updater.update(existing = existing, new = new)

    update("existing", null).shouldBe("existing")
    update("existing", Optional.empty()).shouldBe(null)
    update("existing", Optional.of("new")).shouldBe("new")
  }
}
