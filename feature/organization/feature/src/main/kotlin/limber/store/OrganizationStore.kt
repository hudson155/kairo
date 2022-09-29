package limber.store

import limber.rep.OrganizationRep
import limber.sql.SqlCrudStore
import java.util.UUID

internal class OrganizationStore : SqlCrudStore<OrganizationRep>(OrganizationRep::class) {
  override val tableName: String = "organization.organization"

  fun create(model: OrganizationRep): OrganizationRep =
    jdbi.create(model)

  fun get(guid: UUID): OrganizationRep? =
    jdbi.get(guid)

  fun update(guid: UUID, updater: (OrganizationRep) -> OrganizationRep): OrganizationRep =
    jdbi.update(guid, updater)
}
