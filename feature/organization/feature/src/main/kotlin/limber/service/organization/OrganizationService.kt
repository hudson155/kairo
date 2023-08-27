package limber.service.organization

import com.google.inject.Inject
import limber.feature.event.EventPublisher
import limber.feature.event.EventType
import limber.feature.sql.Sql
import limber.model.organization.OrganizationModel
import limber.service.feature.FeatureInterface
import limber.service.organizationAuth.OrganizationAuthInterface
import limber.service.organizationHostname.OrganizationHostnameInterface
import limber.store.organization.OrganizationStore
import limber.util.updater.Updater

internal class OrganizationService @Inject constructor(
  private val authService: OrganizationAuthInterface,
  private val featureService: FeatureInterface,
  private val hostnameService: OrganizationHostnameInterface,
  private val organizationStore: OrganizationStore,
  publisher: EventPublisher.Factory,
  private val sql: Sql,
) : OrganizationInterface {
  private val publisher: EventPublisher<OrganizationModel> = publisher("organization")

  override suspend fun get(id: String): OrganizationModel? =
    organizationStore.get(id)

  override suspend fun listAll(): List<OrganizationModel> =
    organizationStore.listAll()

  override suspend fun search(search: String): List<OrganizationModel> =
    organizationStore.search(search)

  override suspend fun create(creator: OrganizationModel.Creator): OrganizationModel {
    val organization = organizationStore.create(creator)
    publisher.publish(EventType.Created, organization)
    return organization
  }

  override suspend fun update(id: String, updater: Updater<OrganizationModel.Update>): OrganizationModel {
    val organization = organizationStore.update(id, updater)
    publisher.publish(EventType.Updated, organization)
    return organization
  }

  override suspend fun delete(id: String): OrganizationModel =
    sql.sql {
      featureService.listByOrganization(id).forEach { featureService.delete(it.id) }
      hostnameService.listByOrganization(id).forEach { hostnameService.delete(it.id) }
      authService.getByOrganization(id)?.let { authService.delete(it.id) }
      return@sql organizationStore.delete(id)
    }
}
