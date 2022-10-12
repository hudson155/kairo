package limber.service

import com.google.inject.Inject
import limber.feature.guid.GuidGenerator
import limber.rep.OrganizationHostnameRep
import limber.store.OrganizationHostnameStore
import mu.KLogger
import mu.KotlinLogging
import java.util.UUID

internal class OrganizationHostnameService @Inject constructor(
  private val guidGenerator: GuidGenerator,
  private val hostnameStore: OrganizationHostnameStore,
) {
  private val logger: KLogger = KotlinLogging.logger {}

  fun create(organizationGuid: UUID, creator: OrganizationHostnameRep.Creator): OrganizationHostnameRep {
    logger.info { "Creating organization hostname: $creator." }
    val hostname = OrganizationHostnameRep(
      organizationGuid = organizationGuid,
      guid = guidGenerator.generate(),
      hostname = creator.hostname,
    )
    return hostnameStore.create(hostname)
  }

  fun get(organizationGuid: UUID, guid: UUID): OrganizationHostnameRep? =
    hostnameStore.get(organizationGuid, guid)

  fun delete(organizationGuid: UUID, guid: UUID): OrganizationHostnameRep {
    logger.info { "Deleting organization hostname." }
    return hostnameStore.delete(organizationGuid, guid)
  }
}
