package limber.service.organizationAuth

import com.google.inject.ImplementedBy
import limber.model.organizationAuth.OrganizationAuthModel
import limber.util.updater.Updater

@ImplementedBy(OrganizationAuthService::class)
internal interface OrganizationAuthInterface {
  fun get(authId: String): OrganizationAuthModel?

  fun getByOrganization(organizationId: String): OrganizationAuthModel?

  fun getByHostname(hostname: String): OrganizationAuthModel?

  fun create(creator: OrganizationAuthModel.Creator): OrganizationAuthModel

  fun update(id: String, updater: Updater<OrganizationAuthModel.Update>): OrganizationAuthModel

  fun delete(authId: String): OrganizationAuthModel
}
