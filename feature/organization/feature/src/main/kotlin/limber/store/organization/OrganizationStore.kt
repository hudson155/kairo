package limber.store.organization

import limber.exception.organization.OrganizationDoesNotExist
import limber.feature.sql.SqlStore
import limber.feature.sql.Updater
import limber.rep.organization.OrganizationRep
import org.jdbi.v3.core.kotlin.bindKotlin
import java.util.UUID

internal class OrganizationStore : SqlStore<OrganizationRep>(
  tableName = "organization.organization",
  type = OrganizationRep::class,
) {
  fun create(model: OrganizationRep): OrganizationRep =
    transaction { handle ->
      val query = handle.createQuery(rs("store/organization/create.sql"))
      query.bindKotlin(model)
      return@transaction query.mapToType().single()
    }

  fun update(guid: UUID, updater: Updater<OrganizationRep>): OrganizationRep =
    transaction { handle ->
      val model = updater(get(guid, forUpdate = true) ?: throw OrganizationDoesNotExist())
      val query = handle.createQuery(rs("store/organization/update.sql"))
      query.bindKotlin(model)
      return@transaction query.mapToType().single()
    }
}
