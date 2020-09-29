package io.limberapp.backend.module.auth.store.feature

import com.google.inject.Inject
import com.google.inject.Singleton
import io.limberapp.backend.module.auth.exception.feature.FeatureRoleNotFound
import io.limberapp.backend.module.auth.exception.feature.FeatureRoleOrgRoleIsNotUnique
import io.limberapp.backend.module.auth.model.feature.FeatureRoleModel
import io.limberapp.common.store.SqlStore
import io.limberapp.common.store.isUniqueConstraintViolation
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import java.util.*
import kotlin.reflect.javaType
import kotlin.reflect.typeOf

private const val UNIQ_ORG_ROLE_GUID = "uniq__feature_role__org_role_guid"

@Singleton
internal class FeatureRoleStore @Inject constructor(
  jdbi: Jdbi,
) : SqlStore(jdbi) {
  fun create(model: FeatureRoleModel): FeatureRoleModel =
    withHandle { handle ->
      try {
        handle.createQuery(sqlResource("/store/featureRole/create.sql"))
          .bindKotlin(model)
          .mapTo(FeatureRoleModel::class.java)
          .single()
      } catch (e: UnableToExecuteStatementException) {
        handleCreateError(e)
      }
    }

  fun getByFeatureGuidAndOrgRoleGuids(featureGuid: UUID, orgRoleGuids: Set<UUID>): Set<FeatureRoleModel> =
    withHandle { handle ->
      handle.createQuery(sqlResource("/store/featureRole/getByFeatureGuidAndOrgRoleGuids.sql"))
        .bind("featureGuid", featureGuid)
        .bindByType("orgRoleGuids", orgRoleGuids, typeOf<Set<UUID>>().javaType)
        .mapTo(FeatureRoleModel::class.java)
        .toSet()
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
      try {
        handle.createQuery(sqlResource("/store/featureRole/update.sql"))
          .bind("featureGuid", featureGuid)
          .bind("featureRoleGuid", featureRoleGuid)
          .bindKotlin(update)
          .mapTo(FeatureRoleModel::class.java)
          .singleNullOrThrow()
      } catch (e: UnableToExecuteStatementException) {
        handleUpdateError(e)
      } ?: throw FeatureRoleNotFound()
    }

  fun delete(featureGuid: UUID, featureRoleGuid: UUID): Unit =
    inTransaction { handle ->
      handle.createUpdate(sqlResource("/store/featureRole/delete.sql"))
        .bind("featureGuid", featureGuid)
        .bind("featureRoleGuid", featureRoleGuid)
        .updateOnly() ?: throw FeatureRoleNotFound()
    }

  private fun handleCreateError(e: UnableToExecuteStatementException): Nothing {
    val error = e.serverErrorMessage ?: throw e
    when {
      error.isUniqueConstraintViolation(UNIQ_ORG_ROLE_GUID) -> throw FeatureRoleOrgRoleIsNotUnique()
      else -> throw e
    }
  }

  private fun handleUpdateError(e: UnableToExecuteStatementException): Nothing {
    val error = e.serverErrorMessage ?: throw e
    when {
      error.isUniqueConstraintViolation(UNIQ_ORG_ROLE_GUID) -> throw FeatureRoleOrgRoleIsNotUnique()
      else -> throw e
    }
  }
}
