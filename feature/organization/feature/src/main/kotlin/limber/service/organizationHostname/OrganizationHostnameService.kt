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

  override fun get(id: String): OrganizationHostnameModel? =
    hostnameStore.get(id)

  override fun listByOrganization(organizationId: String): List<OrganizationHostnameModel> =
    hostnameStore.listByOrganization(organizationId)

  override fun create(creator: OrganizationHostnameModel.Creator): OrganizationHostnameModel {
    val hostname = hostnameStore.create(creator)
    publisher.publish(EventType.Created, hostname)
    return hostname
  }

  override fun delete(id: String): OrganizationHostnameModel {
    val hostname = hostnameStore.delete(id)
    publisher.publish(EventType.Deleted, hostname)
    return hostname
  }
}
