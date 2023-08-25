package limber.service.organizationAuth

import com.google.inject.Inject
import limber.exception.organization.OrganizationDoesNotExist
import limber.exception.organizationAuth.OrganizationAuthIdIsNull
import limber.feature.auth0.Auth0ManagementApi
import limber.feature.auth0.rep.Auth0OrganizationRep
import limber.feature.event.EventPublisher
import limber.feature.event.EventType
import limber.feature.sql.transaction
import limber.model.organizationAuth.OrganizationAuthModel
import limber.service.organization.OrganizationInterface
import limber.store.organizationAuth.OrganizationAuthStore
import limber.util.updater.Updater
import org.jdbi.v3.core.Jdbi

internal class OrganizationAuthService @Inject constructor(
  private val authStore: OrganizationAuthStore,
  private val auth0ManagementApi: Auth0ManagementApi,
  private val jdbi: Jdbi,
  private val organizationService: OrganizationInterface,
  publisher: EventPublisher.Factory,
) : OrganizationAuthInterface {
  private val publisher: EventPublisher<OrganizationAuthModel> = publisher("organization-auth")

  override fun get(authId: String): OrganizationAuthModel? =
    authStore.get(authId)

  override fun getByOrganization(organizationId: String): OrganizationAuthModel? =
    authStore.getByOrganization(organizationId)

  override fun getByHostname(hostname: String): OrganizationAuthModel? =
    authStore.getByHostname(hostname)

  override fun create(creator: OrganizationAuthModel.Creator): OrganizationAuthModel {
    val organization = organizationService.get(creator.organizationId) ?: throw OrganizationDoesNotExist()

    val auth = jdbi.transaction {
      val auth = authStore.create(creator)
      val auth0Organization = auth0ManagementApi.createOrganization(
        creator = Auth0OrganizationRep.Creator(
          name = auth.auth0OrganizationName,
          displayName = organization.name,
        ),
      )
      return@transaction authStore.update(auth.id) { existing ->
        OrganizationAuthModel.Update(
          auth0OrganizationId = auth0Organization.id,
          auth0OrganizationName = existing.auth0OrganizationName,
        )
      }
    }
    publisher.publish(EventType.Created, auth)
    return auth
  }

  override fun update(id: String, updater: Updater<OrganizationAuthModel.Update>): OrganizationAuthModel {
    val auth = jdbi.transaction {
      val auth = authStore.update(id, updater)
      auth0ManagementApi.updateOrganization(
        organizationId = auth.auth0OrganizationId ?: throw OrganizationAuthIdIsNull(),
        update = Auth0OrganizationRep.Update(
          name = auth.auth0OrganizationName,
        ),
      )
      return@transaction auth
    }
    publisher.publish(EventType.Updated, auth)
    return auth
  }

  override fun delete(authId: String): OrganizationAuthModel {
    val auth = jdbi.transaction {
      val auth = authStore.delete(authId)
      auth0ManagementApi.deleteOrganization(
        organizationId = auth.auth0OrganizationId ?: throw OrganizationAuthIdIsNull(),
      )
      return@transaction auth
    }
    publisher.publish(EventType.Deleted, auth)
    return auth
  }
}
