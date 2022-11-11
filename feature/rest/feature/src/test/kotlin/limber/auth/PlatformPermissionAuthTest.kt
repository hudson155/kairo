package limber.auth

import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

internal class PlatformPermissionAuthTest {
  @Test
  fun `null permissions`() {
    val context = context(null)
    PlatformPermissionAuth(PlatformPermission.FeatureDelete).authorize(context)
      .shouldBeFalse()
  }

  @Test
  fun `non-overlapping permissions`() {
    val context = context(listOf("feature:create"))
    PlatformPermissionAuth(PlatformPermission.FeatureDelete).authorize(context)
      .shouldBeFalse()
  }

  @Test
  fun `overlapping permissions`() {
    val context = context(listOf("feature:create", "feature:delete"))
    PlatformPermissionAuth(PlatformPermission.FeatureDelete).authorize(context)
      .shouldBeTrue()
  }

  private fun context(permissions: List<String>?): RestContext {
    val permissionsString = "[${permissions.orEmpty().joinToString(", ") { "\"$it\"" }}]"

    return RestContext(
      authorize = true,
      claimPrefix = "",
      principal = mockk {
        every { payload } returns mockk {
          every { getClaim("permissions") } returns mockk mockkClaim@{
            every { isMissing } returns (permissions == null)
            every { isNull } returns (permissions == null)
            every { this@mockkClaim.toString() } returns permissionsString
          }
        }
      },
    )
  }
}
