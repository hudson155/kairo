package limber.service.organization

import com.google.inject.ImplementedBy
import limber.model.organization.OrganizationModel
import limber.util.updater.Updater

@ImplementedBy(OrganizationService::class)
internal interface OrganizationInterface {
  fun get(id: String): OrganizationModel?

  fun listAll(): List<OrganizationModel>

  fun search(search: String): List<OrganizationModel>

  fun create(creator: OrganizationModel.Creator): OrganizationModel

  fun update(id: String, updater: Updater<OrganizationModel.Update>): OrganizationModel
}
