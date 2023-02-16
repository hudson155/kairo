package limber.feature.auth0

import com.google.inject.Inject
import limber.util.guid.GuidGenerator

internal class FakeAuth0ManagementApi @Inject constructor(
  private val guidGenerator: GuidGenerator,
) : Auth0ManagementApi {
  override fun createOrganization(name: String): String =
    guidGenerator.generate().toString()
}
