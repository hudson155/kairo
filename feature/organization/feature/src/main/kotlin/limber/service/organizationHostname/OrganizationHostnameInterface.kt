package limber.service.organizationHostname

import com.google.inject.ImplementedBy
import limber.model.organizationHostname.OrganizationHostnameModel

@ImplementedBy(OrganizationHostnameService::class)
internal interface OrganizationHostnameInterface {
  fun get(id: String): OrganizationHostnameModel?

  fun listByOrganization(organizationId: String): List<OrganizationHostnameModel>

  fun create(creator: OrganizationHostnameModel.Creator): OrganizationHostnameModel

  fun delete(id: String): OrganizationHostnameModel
}
