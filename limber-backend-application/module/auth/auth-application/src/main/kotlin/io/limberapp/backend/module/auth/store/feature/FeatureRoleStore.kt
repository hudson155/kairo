package io.limberapp.backend.module.auth.store.feature

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import com.piperframework.store.isUniqueConstraintViolation
import io.limberapp.backend.module.auth.exception.feature.FeatureRoleNotFound
import io.limberapp.backend.module.auth.exception.feature.FeatureRoleOrgRoleIsNotUnique
import io.limberapp.backend.module.auth.model.feature.FeatureRoleModel
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import java.util.*

private const val FEATURE_ROLE_UNIQUE_CONSTRAINT = "feature_role_feature_guid_org_role_guid_key"

internal class FeatureRoleStore @Inject constructor(jdbi: Jdbi) : SqlStore(jdbi) {
  fun create(model: FeatureRoleModel): FeatureRoleModel =
    withHandle { handle ->
      return@withHandle try {
        handle.createQuery(sqlResource("/store/featureRole/create.sql"))
          .bindKotlin(model)
          .mapTo(FeatureRoleModel::class.java)
          .one()
      } catch (e: UnableToExecuteStatementException) {
        handleCreateError(e)
      }
    }

  fun get(featureGuid: UUID, featureRoleGuid: UUID): FeatureRoleModel? =
    withHandle { handle ->
      handle.createQuery(sqlResource("/store/featureRole/get.sql"))
        .bind("featureGuid", featureGuid)
        .bind("featureRoleGuid", featureRoleGuid)
        .mapTo(FeatureRoleModel::class.java)
        .findOne().orElse(null)
    }

  fun getByFeatureGuid(featureGuid: UUID): Set<FeatureRoleModel> =
    withHandle { handle ->
      handle.createQuery(sqlResource("/store/featureRole/getByFeatureGuid.sql"))
        .bind("featureGuid", featureGuid)
        .mapTo(FeatureRoleModel::class.java)
        .toSet()
    }

  fun update(featureGuid: UUID, featureRoleGuid: UUID, update: FeatureRoleModel.Update): FeatureRoleModel =
    inTransaction { handle ->
      val updateCount = try {
        handle.createUpdate(sqlResource("/store/featureRole/update.sql"))
          .bind("featureGuid", featureGuid)
          .bind("featureRoleGuid", featureRoleGuid)
          .bindKotlin(update)
          .execute()
      } catch (e: UnableToExecuteStatementException) {
        handleUpdateError(e)
      }
      return@inTransaction when (updateCount) {
        0 -> throw FeatureRoleNotFound()
        1 -> checkNotNull(get(featureGuid, featureRoleGuid))
        else -> badSql()
      }
    }

  fun delete(featureGuid: UUID, featureRoleGuid: UUID) =
    inTransaction { handle ->
      val updateCount = handle.createUpdate(sqlResource("/store/featureRole/delete.sql"))
        .bind("featureGuid", featureGuid)
        .bind("featureRoleGuid", featureRoleGuid)
        .execute()
      return@inTransaction when (updateCount) {
        0 -> throw FeatureRoleNotFound()
        1 -> Unit
        else -> badSql()
      }
    }

  private fun handleCreateError(e: UnableToExecuteStatementException): Nothing {
    val error = e.serverErrorMessage ?: throw e
    when {
      error.isUniqueConstraintViolation(FEATURE_ROLE_UNIQUE_CONSTRAINT) -> throw FeatureRoleOrgRoleIsNotUnique()
      else -> throw e
    }
  }

  private fun handleUpdateError(e: UnableToExecuteStatementException): Nothing {
    val error = e.serverErrorMessage ?: throw e
    when {
      error.isUniqueConstraintViolation(FEATURE_ROLE_UNIQUE_CONSTRAINT) -> throw FeatureRoleOrgRoleIsNotUnique()
      else -> throw e
    }
  }
}
