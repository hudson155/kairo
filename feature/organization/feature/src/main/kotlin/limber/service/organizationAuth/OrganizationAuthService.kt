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

  fun get(authGuid: UUID): OrganizationAuthModel? =
    authStore.get(authGuid)

  fun getByOrganization(organizationGuid: UUID): OrganizationAuthModel? =
    authStore.getByOrganization(organizationGuid)

  fun getByHostname(hostname: String): OrganizationAuthModel? =
    authStore.getByHostname(hostname)

  fun create(organizationGuid: UUID, creator: OrganizationAuthRep.Creator): OrganizationAuthModel {
    logger.info { "Creating organization auth: $creator." }
    return authStore.create(authMapper(organizationGuid, creator))
  }

  fun delete(authGuid: UUID): OrganizationAuthModel {
    logger.info { "Deleting organization." }
    return authStore.delete(authGuid)
  }
}
