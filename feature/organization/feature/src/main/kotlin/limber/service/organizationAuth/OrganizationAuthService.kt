package limber.service.organizationAuth

import com.google.inject.Inject
import limber.rep.organizationAuth.OrganizationAuthRep
import limber.store.organizationAuth.OrganizationAuthStore
import limber.util.guid.GuidGenerator
import mu.KLogger
import mu.KotlinLogging
import java.util.UUID

internal class OrganizationAuthService @Inject constructor(
  private val guidGenerator: GuidGenerator,
  private val authStore: OrganizationAuthStore,
) {
  private val logger: KLogger = KotlinLogging.logger {}

  fun getByOrganization(organizationGuid: UUID): OrganizationAuthRep? =
    authStore.getByOrganization(organizationGuid)

  fun getByHostname(hostname: String): OrganizationAuthRep? =
    authStore.getByHostname(hostname)

  fun set(organizationGuid: UUID, creator: OrganizationAuthRep.Creator): OrganizationAuthRep {
    logger.info { "Setting organization auth: $creator." }
    val organization = OrganizationAuthRep(
      organizationGuid = organizationGuid,
      guid = guidGenerator.generate(),
      auth0OrganizationId = creator.auth0OrganizationId,
    )
    return authStore.set(organization)
  }

  fun deleteByOrganization(organizationGuid: UUID): OrganizationAuthRep {
    logger.info { "Deleting organization." }
    return authStore.deleteByOrganization(organizationGuid)
  }
}
