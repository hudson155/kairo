package limber.service.organization

import com.google.inject.Inject
import limber.feature.event.EventPublisher
import limber.feature.event.EventType
import limber.model.organization.OrganizationModel
import limber.store.organization.OrganizationStore
import limber.util.updater.Updater

internal class OrganizationService @Inject constructor(
  private val organizationStore: OrganizationStore,
  publisher: EventPublisher.Factory,
) : OrganizationInterface {
  private val publisher: EventPublisher<OrganizationModel> = publisher("organization")

  override fun get(id: String): OrganizationModel? =
    organizationStore.get(id)

  override fun listAll(): List<OrganizationModel> =
    organizationStore.listAll()

  override fun search(search: String): List<OrganizationModel> =
    organizationStore.search(search)

  override fun create(creator: OrganizationModel.Creator): OrganizationModel {
    val organization = organizationStore.create(creator)
    publisher.publish(EventType.Created, organization)
    return organization
  }

  override fun update(id: String, updater: Updater<OrganizationModel.Update>): OrganizationModel {
    val organization = organizationStore.update(id, updater)
    publisher.publish(EventType.Updated, organization)
    return organization
  }
}
