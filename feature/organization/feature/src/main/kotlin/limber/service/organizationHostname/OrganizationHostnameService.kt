package limber.service.organizationHostname

import com.google.inject.Inject
import limber.feature.event.EventPublisher
import limber.feature.event.EventType
import limber.model.organizationHostname.OrganizationHostnameModel
import limber.store.organizationHostname.OrganizationHostnameStore

internal class OrganizationHostnameService @Inject constructor(
  private val hostnameStore: OrganizationHostnameStore,
  publisher: EventPublisher.Factory,
) : OrganizationHostnameInterface {
  private val publisher: EventPublisher<OrganizationHostnameModel> = publisher("organization-hostname")

  override suspend fun get(id: String): OrganizationHostnameModel? =
    hostnameStore.get(id)

  override suspend fun listByOrganization(organizationId: String): List<OrganizationHostnameModel> =
    hostnameStore.listByOrganization(organizationId)

  override suspend fun create(creator: OrganizationHostnameModel.Creator): OrganizationHostnameModel {
    val hostname = hostnameStore.create(creator)
    publisher.publish(EventType.Created, hostname)
    return hostname
  }

  override suspend fun delete(id: String): OrganizationHostnameModel {
    val hostname = hostnameStore.delete(id)
    publisher.publish(EventType.Deleted, hostname)
    return hostname
  }
}
