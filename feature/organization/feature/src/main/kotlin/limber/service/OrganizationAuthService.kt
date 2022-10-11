package limber.service

import com.google.inject.Inject
import limber.feature.guid.GuidGenerator
import limber.rep.OrganizationAuthRep
import limber.store.OrganizationAuthStore
import mu.KLogger
import mu.KotlinLogging
import java.util.UUID

internal class OrganizationAuthService @Inject constructor(
  private val guidGenerator: GuidGenerator,
  private val authStore: OrganizationAuthStore,
) {
  private val logger: KLogger = KotlinLogging.logger {}

  fun set(organizationGuid: UUID, creator: OrganizationAuthRep.Creator): OrganizationAuthRep {
    logger.info { "Setting organization auth: $creator." }
    val organization = OrganizationAuthRep(
      organizationGuid = organizationGuid,
      guid = guidGenerator.generate(),
      auth0OrganizationId = creator.auth0OrganizationId,
    )
    return authStore.set(organization)
  }

  fun getByOrganization(organizationGuid: UUID): OrganizationAuthRep? =
    authStore.getByOrganization(organizationGuid)
}
