package limber.store

import limber.exception.ConflictException
import limber.exception.UnprocessableException
import limber.rep.FeatureRep
import limber.sql.SqlStore
import limber.sql.isForeignKeyViolation
import limber.sql.isUniqueViolation
import org.jdbi.v3.core.kotlin.bindKotlin
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import org.postgresql.util.ServerErrorMessage
import java.util.UUID

internal class FeatureStore : SqlStore<FeatureRep>(FeatureRep::class) {
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

  fun get(organizationGuid: UUID, guid: UUID): FeatureRep? =
    get(organizationGuid, guid, forUpdate = false)

  private fun get(organizationGuid: UUID, guid: UUID, forUpdate: Boolean): FeatureRep? =
    handle { handle ->
      val query = handle.createQuery(rs("store/feature/get.sql"))
      query.define("lockingClause", if (forUpdate) "for no key update" else "")
      query.bind("organizationGuid", organizationGuid)
      query.bind("guid", guid)
      return@handle query.mapToType().singleNullOrThrow()
    }

  fun getByOrganization(organizationGuid: UUID): List<FeatureRep> =
    handle { handle ->
      val query = handle.createQuery(rs("store/feature/getByOrganization.sql"))
      query.bind("organizationGuid", organizationGuid)
      return@handle query.mapToType().toList()
    }

  override fun ServerErrorMessage.onError(e: UnableToExecuteStatementException) {
    when {
      isForeignKeyViolation("fk__feature__organization_guid") ->
        organizationDoesNotExist()
      isUniqueViolation("uq__feature__root_path") ->
        rootPathAlreadyTaken()
    }
  }

  private fun organizationDoesNotExist(): Nothing =
    throw UnprocessableException("Organization does not exist.")

  private fun rootPathAlreadyTaken(): Nothing =
    throw ConflictException("Root path already taken.")
}
