package limber.service.organizationAuth

import com.google.inject.Inject
import limber.feature.auth0.Auth0ManagementApi
import limber.feature.sql.transaction
import limber.feature.sql.update
import limber.model.organizationAuth.OrganizationAuthModel
import limber.rep.organizationAuth.OrganizationAuthRep
import limber.store.organizationAuth.OrganizationAuthStore
import mu.KLogger
import mu.KotlinLogging
import org.jdbi.v3.core.Jdbi
import java.util.UUID

internal class OrganizationAuthService @Inject constructor(
  private val authStore: OrganizationAuthStore,
  private val auth0ManagementApi: Auth0ManagementApi,
  private val jdbi: Jdbi,
) {
  private val logger: KLogger = KotlinLogging.logger {}

  fun get(authGuid: UUID): OrganizationAuthModel? =
    authStore.get(authGuid)

  fun getByOrganization(organizationGuid: UUID): OrganizationAuthModel? =
    authStore.getByOrganization(organizationGuid)

  fun getByHostname(hostname: String): OrganizationAuthModel? =
    authStore.getByHostname(hostname)

  fun create(creator: OrganizationAuthModel.Creator): OrganizationAuthModel {
    logger.info { "Creating organization auth: $creator." }
    return jdbi.transaction {
      val auth = authStore.create(creator)
      val auth0OrganizationId = auth0ManagementApi.createOrganization(
        name = auth.auth0OrganizationName,
      )
      return@transaction authStore.update(auth.guid) { existing ->
        OrganizationAuthModel.Updater(
          auth0OrganizationId = auth0OrganizationId,
          auth0OrganizationName = existing.auth0OrganizationName,
        )
      }
    }
  }

  fun update(guid: UUID, updater: OrganizationAuthRep.Updater): OrganizationAuthModel {
    logger.info { "Updating organization auth: $updater." }
    return jdbi.transaction {
      val auth = authStore.update(guid) { existing ->
        OrganizationAuthModel.Updater(
          auth0OrganizationId = existing.auth0OrganizationId,
          auth0OrganizationName = update(
            existing = existing.auth0OrganizationName,
            new = updater.auth0OrganizationName,
          ),
        )
      }
      auth0ManagementApi.updateOrganization(
        organizationId = checkNotNull(auth.auth0OrganizationId) {
          "The Auth0 organization ID should only be null during the creation process."
        },
        name = auth.auth0OrganizationName,
      )
      return@transaction auth
    }
  }

  fun delete(authGuid: UUID): OrganizationAuthModel {
    logger.info { "Deleting organization." }
    return jdbi.transaction {
      val auth = authStore.delete(authGuid)
      auth0ManagementApi.deleteOrganization(
        organizationId = checkNotNull(auth.auth0OrganizationId) {
          "The Auth0 organization ID should only be null during the creation process."
        },
      )
      return@transaction auth
    }
  }
}
