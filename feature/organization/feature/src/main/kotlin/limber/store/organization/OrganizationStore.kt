package limber.store.organization

import limber.exception.organization.OrganizationDoesNotExist
import limber.feature.sql.SqlStore
import limber.feature.sql.Updater
import limber.model.organization.OrganizationModel
import org.jdbi.v3.core.kotlin.bindKotlin
import java.util.UUID

internal class OrganizationStore : SqlStore<OrganizationModel>(
  tableName = "organization.organization",
  type = OrganizationModel::class,
) {
  fun getAll(): List<OrganizationModel> =
    handle { handle ->
      val query = handle.createQuery(rs("store/organization/getAll.sql"))
      return@handle query.mapToType().toList()
    }

  fun create(model: OrganizationModel.Creator): OrganizationModel =
    transaction { handle ->
      val query = handle.createQuery(rs("store/organization/create.sql"))
      query.bindKotlin(model)
      return@transaction query.mapToType().single()
    }

  fun update(
    guid: UUID,
    updater: Updater<OrganizationModel, OrganizationModel.Updater>,
  ): OrganizationModel =
    transaction { handle ->
      val model = updater(get(guid, forUpdate = true) ?: throw OrganizationDoesNotExist())
      val query = handle.createQuery(rs("store/organization/update.sql"))
      query.bind("guid", guid)
      query.bindKotlin(model)
      return@transaction query.mapToType().single()
    }
}
