package limber.service.organizationHostname

import com.google.inject.Inject
import limber.model.organizationHostname.OrganizationHostnameModel
import limber.store.organizationHostname.OrganizationHostnameStore

internal class OrganizationHostnameService @Inject constructor(
  private val hostnameStore: OrganizationHostnameStore,
) {
  fun get(id: String): OrganizationHostnameModel? =
    hostnameStore.get(id)

  fun create(creator: OrganizationHostnameModel.Creator): OrganizationHostnameModel =
    hostnameStore.create(creator)

  fun delete(id: String): OrganizationHostnameModel =
    hostnameStore.delete(id)
}
