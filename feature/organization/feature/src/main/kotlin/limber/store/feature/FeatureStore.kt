package limber.store.feature

import limber.exception.feature.FeatureDoesNotExist
import limber.exception.feature.RootPathAlreadyTaken
import limber.exception.organization.OrganizationDoesNotExist
import limber.feature.sql.SqlStore
import limber.feature.sql.Updater
import limber.feature.sql.isForeignKeyViolation
import limber.feature.sql.isUniqueViolation
import limber.rep.feature.FeatureRep
import org.jdbi.v3.core.kotlin.bindKotlin
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import org.postgresql.util.ServerErrorMessage
import java.util.UUID

internal class FeatureStore : SqlStore<FeatureRep>(
  tableName = "organization.feature",
  type = FeatureRep::class,
) {
  fun getByOrganization(organizationGuid: UUID): List<FeatureRep> =
    handle { handle ->
      val query = handle.createQuery(rs("store/feature/getByOrganization.sql"))
      query.bind("organizationGuid", organizationGuid)
      return@handle query.mapToType().toList()
    }

  fun create(model: FeatureRep): FeatureRep =
    transaction { handle ->
      var updated = model
      if (getByOrganization(updated.organizationGuid).none { it.isDefault }) {
        // If the organization doesn't have a default feature, this one should be it!
        updated = updated.copy(isDefault = true)
      }
      val query = handle.createQuery(rs("store/feature/create.sql"))
      query.bindKotlin(updated)
      return@transaction query.mapToType().single()
    }

  fun setDefault(guid: UUID): List<FeatureRep> =
    transaction { handle ->
      val query = handle.createQuery(rs("store/feature/setDefaultByOrganization.sql"))
      query.bind("guid", guid)
      return@transaction query.mapToType().toList()
    }

  fun update(guid: UUID, updater: Updater<FeatureRep>): FeatureRep =
    transaction { handle ->
      val model = updater(get(guid, forUpdate = true) ?: throw FeatureDoesNotExist())
      val query = handle.createQuery(rs("store/feature/update.sql"))
      query.bindKotlin(model)
      return@transaction query.mapToType().single()
    }

  fun delete(guid: UUID): FeatureRep =
    transaction { handle ->
      val query = handle.createQuery(rs("store/feature/delete.sql"))
      query.bind("guid", guid)
      return@transaction query.mapToType().singleNullOrThrow() ?: throw FeatureDoesNotExist()
    }

  override fun ServerErrorMessage.onError(e: UnableToExecuteStatementException) {
    when {
      isForeignKeyViolation("fk__feature__organization_guid") ->
        throw OrganizationDoesNotExist()
      isUniqueViolation("uq__feature__root_path") ->
        throw RootPathAlreadyTaken()
    }
  }
}
