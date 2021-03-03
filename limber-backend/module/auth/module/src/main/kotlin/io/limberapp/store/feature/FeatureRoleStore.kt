package io.limberapp.store.feature

import com.google.inject.Inject
import com.google.inject.Singleton
import io.limberapp.exception.feature.FeatureRoleNotFound
import io.limberapp.exception.feature.FeatureRoleOrgRoleIsNotUnique
import io.limberapp.model.feature.FeatureRoleModel
import io.limberapp.sql.store.SqlStore
import io.limberapp.sql.store.isUniqueConstraintViolation
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import java.util.UUID

private const val UNIQ_ORG_ROLE_GUID = "uniq__feature_role__org_role_guid"

@Singleton
internal class FeatureRoleStore @Inject constructor(jdbi: Jdbi) : SqlStore(jdbi) {
  fun create(model: FeatureRoleModel): FeatureRoleModel =
      withHandle { handle ->
        try {
          handle.createQuery(sqlResource("store/featureRole/create.sql"))
              .bindKotlin(model)
              .mapTo(FeatureRoleModel::class.java)
              .single()
        } catch (e: UnableToExecuteStatementException) {
          handleCreateError(e)
        }
      }

  operator fun get(featureRoleGuid: UUID): FeatureRoleModel? =
      withHandle { handle ->
        handle.createQuery(sqlResource("store/featureRole/get.sql"))
            .bind("featureRoleGuid", featureRoleGuid)
            .mapTo(FeatureRoleModel::class.java)
            .singleNullOrThrow()
      }

  fun getByOrgRoleGuids(featureGuid: UUID, orgRoleGuids: Set<UUID>): Set<FeatureRoleModel> =
      withHandle { handle ->
        handle.createQuery(sqlResource("store/featureRole/getByOrgRoleGuids.sql"))
            .bind("featureGuid", featureGuid)
            .bindList("orgRoleGuids", orgRoleGuids)
            .mapTo(FeatureRoleModel::class.java)
            .toSet()
      }

  fun getByFeatureGuid(featureGuid: UUID): Set<FeatureRoleModel> =
      withHandle { handle ->
        handle.createQuery(sqlResource("store/featureRole/getByFeatureGuid.sql"))
            .bind("featureGuid", featureGuid)
            .mapTo(FeatureRoleModel::class.java)
            .toSet()
      }

  fun update(featureRoleGuid: UUID, update: FeatureRoleModel.Update): FeatureRoleModel =
      inTransaction { handle ->
        handle.createQuery(sqlResource("store/featureRole/update.sql"))
            .bind("featureRoleGuid", featureRoleGuid)
            .bindKotlin(update)
            .mapTo(FeatureRoleModel::class.java)
            .singleNullOrThrow() ?: throw FeatureRoleNotFound()
      }

  fun delete(featureRoleGuid: UUID): Unit =
      inTransaction { handle ->
        handle.createUpdate(sqlResource("store/featureRole/delete.sql"))
            .bind("featureRoleGuid", featureRoleGuid)
            .singleNullOrThrow() ?: throw FeatureRoleNotFound()
      }

  private fun handleCreateError(e: UnableToExecuteStatementException): Nothing {
    val error = e.serverErrorMessage ?: throw e
    when {
      error.isUniqueConstraintViolation(UNIQ_ORG_ROLE_GUID) -> throw FeatureRoleOrgRoleIsNotUnique()
      else -> throw e
    }
  }
}
