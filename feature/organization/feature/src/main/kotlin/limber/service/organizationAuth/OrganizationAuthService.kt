package limber.service.organizationAuth

import com.google.inject.Inject
import limber.mapper.organizationAuth.OrganizationAuthMapper
import limber.model.organizationAuth.OrganizationAuthModel
import limber.rep.organizationAuth.OrganizationAuthRep
import limber.store.organizationAuth.OrganizationAuthStore
import mu.KLogger
import mu.KotlinLogging
import java.util.UUID

internal class OrganizationAuthService @Inject constructor(
  private val authMapper: OrganizationAuthMapper,
  private val authStore: OrganizationAuthStore,
) {
  private val logger: KLogger = KotlinLogging.logger {}

  fun getByOrganization(organizationGuid: UUID): OrganizationAuthModel? =
    authStore.getByOrganization(organizationGuid)

  fun getByHostname(hostname: String): OrganizationAuthModel? =
    authStore.getByHostname(hostname)

  fun set(organizationGuid: UUID, creator: OrganizationAuthRep.Creator): OrganizationAuthModel {
    logger.info { "Setting organization auth: $creator." }
    return authStore.set(authMapper(organizationGuid, creator))
  }

  fun deleteByOrganization(organizationGuid: UUID): OrganizationAuthModel {
    logger.info { "Deleting organization." }
    return authStore.deleteByOrganization(organizationGuid)
  }
}
