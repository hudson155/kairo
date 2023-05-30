package limber.store.feature

import limber.exception.feature.FeatureDoesNotExist
import limber.exception.feature.RootPathAlreadyTaken
import limber.exception.organization.OrganizationDoesNotExist
import limber.feature.sql.SqlStore
import limber.feature.sql.isForeignKeyViolation
import limber.feature.sql.isUniqueViolation
import limber.model.feature.FeatureModel
import limber.util.updater.Updater
import mu.KLogger
import mu.KotlinLogging
import org.jdbi.v3.core.kotlin.bindKotlin
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import org.postgresql.util.ServerErrorMessage
import java.util.UUID

internal class FeatureStore : SqlStore<FeatureModel>(
  tableName = "organization.feature",
  type = FeatureModel::class,
) {
  private val logger: KLogger = KotlinLogging.logger {}

  fun listByOrganization(organizationGuid: UUID): List<FeatureModel> =
    handle { handle ->
      val query = handle.createQuery(rs("store/feature/listByOrganization.sql"))
      query.bind("organizationGuid", organizationGuid)
      return@handle query.mapToType().toList()
    }

  fun create(creator: FeatureModel.Creator): FeatureModel =
    transaction { handle ->
      var updated = creator
      if (listByOrganization(updated.organizationGuid).none { it.isDefault }) {
        // If the organization doesn't have a default feature, this one should be it!
        updated = updated.copy(isDefault = true)
      }
      logger.info { "Creating feature: $updated." }
      val query = handle.createQuery(rs("store/feature/create.sql"))
      query.bindKotlin(updated)
      return@transaction query.mapToType().single()
    }

  fun setDefault(guid: UUID): List<FeatureModel> =
    transaction { handle ->
      val query = handle.createQuery(rs("store/feature/setDefaultByOrganization.sql"))
      query.bind("guid", guid)
      return@transaction query.mapToType().toList()
    }

  fun update(guid: UUID, updater: Updater<FeatureModel.Update>): FeatureModel =
    transaction { handle ->
      val feature = getByGuid(guid, forUpdate = true) ?: throw FeatureDoesNotExist()
      val update = updater(FeatureModel.Update(feature))
      logger.info { "Updating feature: $update." }
      val query = handle.createQuery(rs("store/feature/update.sql"))
      query.bind("guid", guid)
      query.bindKotlin(update)
      return@transaction query.mapToType().single()
    }

  fun delete(guid: UUID): FeatureModel =
    transaction { handle ->
      logger.info { "Deleting feature." }
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
