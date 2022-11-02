package limber.auth

import com.fasterxml.jackson.core.type.TypeReference
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import java.util.UUID

internal class FeatureAuthTest {
  @Test
  fun `null features`() {
    val featureGuid = UUID.randomUUID()
    val context = context(null)
    FeatureAuth(featureGuid).authorize(context)
      .shouldBeFalse()
  }

  @Test
  fun `non-overlapping features`() {
    val featureGuid = UUID.randomUUID()
    val context = context(mapOf(UUID.randomUUID() to FeatureClaim))
    FeatureAuth(featureGuid).authorize(context)
      .shouldBeFalse()
  }

  @Test
  fun `overlapping features`() {
    val featureGuid = UUID.randomUUID()
    val context = context(mapOf(UUID.randomUUID() to FeatureClaim, featureGuid to FeatureClaim))
    FeatureAuth(featureGuid).authorize(context)
      .shouldBeTrue()
  }

  private fun context(features: Map<UUID, FeatureClaim>?): RestContext =
    mockk {
      every { getClaim("features", any<TypeReference<*>>()) } returns features
    }
}
