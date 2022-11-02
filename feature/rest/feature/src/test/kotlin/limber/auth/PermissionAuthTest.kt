package limber.auth

import com.fasterxml.jackson.core.type.TypeReference
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

internal class PermissionAuthTest {
  @Test
  fun `null permissions`() {
    val context = context(null)
    Auth.Permission("somePermission").authorize(context)
      .shouldBeFalse()
  }

  @Test
  fun `non-overlapping permissions`() {
    val context = context(listOf("someOtherPermission"))
    Auth.Permission("somePermission").authorize(context)
      .shouldBeFalse()
  }

  @Test
  fun `overlapping permissions`() {
    val context = context(listOf("somePermission", "someOtherPermission"))
    Auth.Permission("somePermission").authorize(context)
      .shouldBeTrue()
  }

  private fun context(permissions: List<String>?): RestContext =
    mockk {
      every { getClaim("permissions", any<TypeReference<*>>()) } returns permissions
    }
}
