package limber.service.organizationHostname

import com.google.inject.Inject
import limber.mapper.organizationHostname.OrganizationHostnameMapper
import limber.model.organizationHostname.OrganizationHostnameModel
import limber.rep.organizationHostname.OrganizationHostnameRep
import limber.store.organizationHostname.OrganizationHostnameStore
import mu.KLogger
import mu.KotlinLogging
import java.util.UUID

internal class OrganizationHostnameService @Inject constructor(
  private val hostnameMapper: OrganizationHostnameMapper,
  private val hostnameStore: OrganizationHostnameStore,
) {
  private val logger: KLogger = KotlinLogging.logger {}

  fun get(guid: UUID): OrganizationHostnameModel? =
    hostnameStore.get(guid)

  fun create(organizationGuid: UUID, creator: OrganizationHostnameRep.Creator): OrganizationHostnameModel {
    logger.info { "Creating organization hostname: $creator." }
    return hostnameStore.create(hostnameMapper(organizationGuid, creator))
  }

  fun delete(guid: UUID): OrganizationHostnameModel {
    logger.info { "Deleting organization hostname." }
    return hostnameStore.delete(guid)
  }
}
