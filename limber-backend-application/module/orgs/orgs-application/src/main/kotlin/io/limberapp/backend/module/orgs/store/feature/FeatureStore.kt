package io.limberapp.backend.module.orgs.store.feature

import com.google.inject.Inject
import com.google.inject.Singleton
import com.piperframework.store.SqlStore
import com.piperframework.store.isForeignKeyViolation
import com.piperframework.store.isUniqueConstraintViolation
import io.limberapp.backend.module.orgs.exception.feature.FeatureNotFound
import io.limberapp.backend.module.orgs.exception.feature.FeaturePathIsNotUnique
import io.limberapp.backend.module.orgs.exception.feature.FeatureRankIsNotUnique
import io.limberapp.backend.module.orgs.exception.org.OrgNotFound
import io.limberapp.backend.module.orgs.model.org.FeatureModel
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import java.util.*

private const val ORG_GUID_FOREIGN_KEY = "feature_org_guid_fkey"
private const val PATH_UNIQUE_CONSTRAINT = "feature_org_guid_lower_idx"
private const val RANK_UNIQUE_CONSTRAINT = "feature_org_guid_rank_key"

@Singleton
internal class FeatureStore @Inject constructor(jdbi: Jdbi) : SqlStore(jdbi) {
  fun create(model: FeatureModel): FeatureModel =
    withHandle { handle ->
      return@withHandle try {
        handle.createQuery(sqlResource("/store/feature/create.sql"))
          .bindKotlin(model)
          .mapTo(FeatureModel::class.java)
          .one()
      } catch (e: UnableToExecuteStatementException) {
        handleCreateError(e)
      }
    }

  fun get(orgGuid: UUID, featureGuid: UUID): FeatureModel? =
    withHandle { handle ->
      handle.createQuery(sqlResource("/store/feature/get.sql"))
        .bind("orgGuid", orgGuid)
        .bind("featureGuid", featureGuid)
        .mapTo(FeatureModel::class.java)
        .findOne().orElse(null)
    }

  fun getByFeatureGuid(featureGuid: UUID): FeatureModel? =
    withHandle { handle ->
      handle.createQuery(sqlResource("/store/feature/getByFeatureGuid.sql"))
        .bind("featureGuid", featureGuid)
        .mapTo(FeatureModel::class.java)
        .findOne().orElse(null)
    }

  fun getByOrgGuid(orgGuid: UUID): List<FeatureModel> =
    withHandle { handle ->
      handle.createQuery(sqlResource("/store/feature/getByOrgGuid.sql"))
        .bind("orgGuid", orgGuid)
        .mapTo(FeatureModel::class.java)
        .toList()
    }

  fun update(orgGuid: UUID, featureGuid: UUID, update: FeatureModel.Update): FeatureModel =
    inTransaction { handle ->
      if (update.isDefaultFeature == true) {
        handle.createUpdate(sqlResource("/store/feature/removeDefaultFeaturesByOrgGuid.sql"))
          .bind("orgGuid", orgGuid)
          .execute()
      }
      val updateCount = try {
        handle.createUpdate(sqlResource("/store/feature/update.sql"))
          .bind("orgGuid", orgGuid)
          .bind("featureGuid", featureGuid)
          .bindKotlin(update)
          .execute()
      } catch (e: UnableToExecuteStatementException) {
        handleUpdateError(e)
      }
      return@inTransaction when (updateCount) {
        0 -> throw FeatureNotFound()
        1 -> checkNotNull(get(orgGuid, featureGuid))
        else -> badSql()
      }
    }

  fun delete(orgGuid: UUID, featureGuid: UUID) =
    inTransaction { handle ->
      val updateCount = handle.createUpdate(sqlResource("/store/feature/delete.sql"))
        .bind("orgGuid", orgGuid)
        .bind("featureGuid", featureGuid)
        .execute()
      return@inTransaction when (updateCount) {
        0 -> throw FeatureNotFound()
        1 -> Unit
        else -> badSql()
      }
    }

  private fun handleCreateError(e: UnableToExecuteStatementException): Nothing {
    val error = e.serverErrorMessage ?: throw e
    when {
      error.isForeignKeyViolation(ORG_GUID_FOREIGN_KEY) -> throw OrgNotFound()
      error.isUniqueConstraintViolation(PATH_UNIQUE_CONSTRAINT) -> throw FeaturePathIsNotUnique()
      error.isUniqueConstraintViolation(RANK_UNIQUE_CONSTRAINT) -> throw FeatureRankIsNotUnique()
      else -> throw e
    }
  }

  private fun handleUpdateError(e: UnableToExecuteStatementException): Nothing {
    val error = e.serverErrorMessage ?: throw e
    when {
      error.isUniqueConstraintViolation(PATH_UNIQUE_CONSTRAINT) -> throw FeaturePathIsNotUnique()
      error.isUniqueConstraintViolation(RANK_UNIQUE_CONSTRAINT) -> throw FeatureRankIsNotUnique()
      else -> throw e
    }
  }
}
