package io.limberapp.backend.module.auth.store.feature

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import com.piperframework.store.isUniqueConstraintViolation
import com.piperframework.util.singleNullOrThrow
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
        it.createQuery(sqlResource("create"))
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

  fun existsAndHasFeatureGuid(featureRoleGuid: UUID, featureGuid: UUID): Boolean {
    val model = get(featureRoleGuid) ?: return false
    return model.featureGuid == featureGuid
  }

  fun get(featureRoleGuid: UUID): FeatureRoleModel? {
    return jdbi.withHandle<FeatureRoleModel?, Exception> {
      it.createQuery("SELECT * FROM auth.feature_role WHERE guid = :guid")
        .bind("guid", featureRoleGuid)
        .mapTo(FeatureRoleModel::class.java)
        .singleNullOrThrow()
    }
  }

  fun getByFeatureGuid(featureGuid: UUID): Set<FeatureRoleModel> {
    return jdbi.withHandle<Set<FeatureRoleModel>, Exception> {
      it.createQuery("SELECT * FROM auth.feature_role WHERE feature_guid = :featureGuid")
        .bind("featureGuid", featureGuid)
        .mapTo(FeatureRoleModel::class.java)
        .toSet()
    }
  }

  fun update(featureRoleGuid: UUID, update: FeatureRoleModel.Update): FeatureRoleModel {
    return jdbi.inTransaction<FeatureRoleModel, Exception> {
      val updateCount = try {
        it.createUpdate(sqlResource("update"))
          .bind("guid", featureRoleGuid)
          .bindKotlin(update)
          .execute()
      } catch (e: UnableToExecuteStatementException) {
        handleUpdateError(e)
      }
      return@inTransaction when (updateCount) {
        0 -> throw FeatureRoleNotFound()
        1 -> checkNotNull(get(featureRoleGuid))
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
