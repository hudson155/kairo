package limber.service.organizationHostname

import com.google.inject.Inject
import limber.model.organizationHostname.OrganizationHostnameModel
import limber.store.organizationHostname.OrganizationHostnameStore
import java.util.UUID

internal class OrganizationHostnameService @Inject constructor(
  private val hostnameStore: OrganizationHostnameStore,
) {
  fun get(guid: UUID): OrganizationHostnameModel? =
    hostnameStore.get(guid)

  fun create(creator: OrganizationHostnameModel.Creator): OrganizationHostnameModel =
    hostnameStore.create(creator)

  fun delete(guid: UUID): OrganizationHostnameModel =
    hostnameStore.delete(guid)
}
