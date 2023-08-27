package limber.service.organizationHostname

import com.google.inject.ImplementedBy
import limber.model.organizationHostname.OrganizationHostnameModel

@ImplementedBy(OrganizationHostnameService::class)
internal interface OrganizationHostnameInterface {
  suspend fun get(id: String): OrganizationHostnameModel?

  suspend fun listByOrganization(organizationId: String): List<OrganizationHostnameModel>

  suspend fun create(creator: OrganizationHostnameModel.Creator): OrganizationHostnameModel

  suspend fun delete(id: String): OrganizationHostnameModel
}
