package limber.service.organizationAuth

import com.google.inject.Inject
import limber.feature.auth0.Auth0ManagementApi
import limber.feature.sql.transaction
import limber.mapper.organizationAuth.OrganizationAuthMapper
import limber.model.organizationAuth.OrganizationAuthModel
import limber.rep.organizationAuth.OrganizationAuthRep
import limber.store.organizationAuth.OrganizationAuthStore
import mu.KLogger
import mu.KotlinLogging
import org.jdbi.v3.core.Jdbi
import java.util.UUID

internal class OrganizationAuthService @Inject constructor(
  private val authMapper: OrganizationAuthMapper,
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

  fun create(organizationGuid: UUID, creator: OrganizationAuthRep.Creator): OrganizationAuthModel {
    logger.info { "Creating organization auth: $creator." }
    return jdbi.transaction {
      val auth = authStore.create(authMapper(organizationGuid, creator))
      val auth0OrganizationId = auth0ManagementApi.createOrganization(
        name = auth.auth0OrganizationName,
      )
      return@transaction authStore.update(auth.guid) {
        OrganizationAuthModel.Updater(
          auth0OrganizationId = auth0OrganizationId,
        )
      }
    }
  }

  fun delete(authGuid: UUID): OrganizationAuthModel {
    logger.info { "Deleting organization." }
    return authStore.delete(authGuid)
  }
}
