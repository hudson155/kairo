package limber.service.organizationHostname

import com.google.inject.Inject
import limber.rep.organizationHostname.OrganizationHostnameRep
import limber.store.organizationHostname.OrganizationHostnameStore
import limber.util.guid.GuidGenerator
import mu.KLogger
import mu.KotlinLogging
import java.util.UUID

internal class OrganizationHostnameService @Inject constructor(
  private val guidGenerator: GuidGenerator,
  private val hostnameStore: OrganizationHostnameStore,
) {
  private val logger: KLogger = KotlinLogging.logger {}

  fun get(guid: UUID): OrganizationHostnameRep? =
    hostnameStore.get(guid)

  fun create(organizationGuid: UUID, creator: OrganizationHostnameRep.Creator): OrganizationHostnameRep {
    logger.info { "Creating organization hostname: $creator." }
    val hostname = OrganizationHostnameRep(
      organizationGuid = organizationGuid,
      guid = guidGenerator.generate(),
      hostname = creator.hostname,
    )
    return hostnameStore.create(hostname)
  }

  fun delete(guid: UUID): OrganizationHostnameRep {
    logger.info { "Deleting organization hostname." }
    return hostnameStore.delete(guid)
  }
}
