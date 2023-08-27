package limber.service.organizationAuth

import com.google.inject.ImplementedBy
import limber.model.organizationAuth.OrganizationAuthModel
import limber.util.updater.Updater

@ImplementedBy(OrganizationAuthService::class)
internal interface OrganizationAuthInterface {
  suspend fun get(authId: String): OrganizationAuthModel?

  suspend fun getByOrganization(organizationId: String): OrganizationAuthModel?

  suspend fun getByHostname(hostname: String): OrganizationAuthModel?

  suspend fun create(creator: OrganizationAuthModel.Creator): OrganizationAuthModel

  suspend fun update(id: String, updater: Updater<OrganizationAuthModel.Update>): OrganizationAuthModel

  suspend fun delete(authId: String): OrganizationAuthModel
}
