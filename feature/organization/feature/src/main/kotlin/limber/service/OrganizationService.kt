package limber.service

import com.google.inject.Inject
import limber.feature.guid.GuidGenerator
import limber.rep.OrganizationRep
import limber.store.OrganizationStore
import mu.KLogger
import mu.KotlinLogging
import java.util.UUID

public class OrganizationService @Inject constructor(
  private val guidGenerator: GuidGenerator,
  private val organizationStore: OrganizationStore,
) {
  private val logger: KLogger = KotlinLogging.logger {}

  public fun create(creator: OrganizationRep.Creator): OrganizationRep {
    val organization = OrganizationRep(guid = guidGenerator.generate(), name = creator.name)
    logger.info { "Creating organization: $organization." }
    return organizationStore.create(organization)
  }

  public fun get(organizationGuid: UUID): OrganizationRep? =
    organizationStore.get(organizationGuid)
}
