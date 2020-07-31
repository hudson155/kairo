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

internal class FeatureRoleStore @Inject constructor(private val jdbi: Jdbi) : SqlStore() {
  fun create(model: FeatureRoleModel): FeatureRoleModel {
    return jdbi.withHandle<FeatureRoleModel, Exception> {
      try {
        it.createQuery(sqlResource("/store/featureRole/create.sql"))
          .bindKotlin(model)
          .mapTo(FeatureRoleModel::class.java)
          .single()
      } catch (e: UnableToExecuteStatementException) {
        handleCreateError(e)
      }
    }
  }

  private fun handleCreateError(e: UnableToExecuteStatementException): Nothing {
    val error = e.serverErrorMessage ?: throw e
    if (error.isUniqueConstraintViolation(FEATURE_ROLE_UNIQUE_CONSTRAINT)) throw FeatureRoleOrgRoleIsNotUnique()
    else throw e
  }

  fun get(featureGuid: UUID? = null, featureRoleGuid: UUID? = null): List<FeatureRoleModel> {
    return jdbi.withHandle<List<FeatureRoleModel>, Exception> {
      it.createQuery("SELECT * FROM auth.feature_role WHERE <conditions>").build {
        if (featureGuid != null) {
          conditions.add("feature_guid = :featureGuid")
          bindings["featureGuid"] = featureGuid
        }
        if (featureRoleGuid != null) {
          conditions.add("guid = :featureRoleGuid")
          bindings["featureRoleGuid"] = featureRoleGuid
        }
      }
        .mapTo(FeatureRoleModel::class.java)
        .list()
    }
  }

  fun update(featureRoleGuid: UUID, update: FeatureRoleModel.Update): FeatureRoleModel {
    return jdbi.inTransaction<FeatureRoleModel, Exception> {
      val updateCount = try {
        it.createUpdate(sqlResource("/store/featureRole/update.sql"))
          .bind("guid", featureRoleGuid)
          .bindKotlin(update)
          .execute()
      } catch (e: UnableToExecuteStatementException) {
        handleUpdateError(e)
      }
      return@inTransaction when (updateCount) {
        0 -> throw FeatureRoleNotFound()
        1 -> get(featureRoleGuid = featureRoleGuid).single()
        else -> badSql()
      }
    }
  }

  private fun handleUpdateError(e: UnableToExecuteStatementException): Nothing {
    val error = e.serverErrorMessage ?: throw e
    if (error.isUniqueConstraintViolation(FEATURE_ROLE_UNIQUE_CONSTRAINT)) throw FeatureRoleOrgRoleIsNotUnique()
    else throw e
  }

  fun delete(featureRoleGuid: UUID) {
    jdbi.useTransaction<Exception> {
      val updateCount = it.createUpdate("DELETE FROM auth.feature_role WHERE guid = :guid")
        .bind("guid", featureRoleGuid)
        .execute()
      return@useTransaction when (updateCount) {
        0 -> throw FeatureRoleNotFound()
        1 -> Unit
        else -> badSql()
      }
    }
  }
}
