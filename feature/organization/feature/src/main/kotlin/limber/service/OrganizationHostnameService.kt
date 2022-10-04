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
    val organization = OrganizationHostnameRep(
      guid = guidGenerator.generate(),
      organizationGuid = organizationGuid,
      hostname = creator.hostname,
    )
    return hostnameStore.create(organization)
  }

  fun get(organizationGuid: UUID, hostnameGuid: UUID): OrganizationHostnameRep? =
    hostnameStore.get(organizationGuid, hostnameGuid)
}
