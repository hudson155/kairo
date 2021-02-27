package io.limberapp.store.feature

import com.google.inject.Inject
import com.google.inject.Singleton
import io.limberapp.exception.feature.FeatureNameIsNotUnique
import io.limberapp.exception.feature.FeatureNotFound
import io.limberapp.exception.feature.FeaturePathIsNotUnique
import io.limberapp.exception.feature.FeatureRankIsNotUnique
import io.limberapp.exception.org.OrgNotFound
import io.limberapp.exception.unprocessable
import io.limberapp.model.feature.FeatureModel
import io.limberapp.sql.store.SqlStore
import io.limberapp.sql.store.isForeignKeyViolation
import io.limberapp.sql.store.isUniqueConstraintViolation
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import java.util.UUID

private const val FK_ORG_GUID = "fk__feature__org_guid"
private const val UNIQ_NAME = "uniq__feature__name"
private const val UNIQ_PATH = "uniq__feature__path"
private const val UNIQ_RANK = "uniq__feature__rank"

@Singleton
internal class FeatureStore @Inject constructor(jdbi: Jdbi) : SqlStore(jdbi) {
  fun create(model: FeatureModel): FeatureModel =
      withHandle { handle ->
        try {
          handle.createQuery(sqlResource("store/feature/create.sql"))
              .bindKotlin(model)
              .mapTo(FeatureModel::class.java)
              .single()
        } catch (e: UnableToExecuteStatementException) {
          handleCreateError(e)
        }
      }

  operator fun get(featureGuid: UUID): FeatureModel? =
      withHandle { handle ->
        handle.createQuery(sqlResource("store/feature/get.sql"))
            .bind("featureGuid", featureGuid)
            .mapTo(FeatureModel::class.java)
            .singleNullOrThrow()
      }

  fun getByOrgGuid(orgGuid: UUID): List<FeatureModel> =
      withHandle { handle ->
        handle.createQuery(sqlResource("store/feature/getByOrgGuid.sql"))
            .bind("orgGuid", orgGuid)
            .mapTo(FeatureModel::class.java)
            .toList()
      }

  fun update(featureGuid: UUID, update: FeatureModel.Update): FeatureModel =
      inTransaction { handle ->
        if (update.isDefaultFeature == true) {
          handle.createUpdate(sqlResource("store/feature/removeDefaultFeaturesInSameOrg.sql"))
              .bind("featureGuid", featureGuid)
              .execute()
        }
        try {
          handle.createQuery(sqlResource("store/feature/update.sql"))
              .bind("featureGuid", featureGuid)
              .bindKotlin(update)
              .mapTo(FeatureModel::class.java)
              .singleNullOrThrow() ?: throw FeatureNotFound()
        } catch (e: UnableToExecuteStatementException) {
          handleUpdateError(e)
        }
      }

  fun delete(featureGuid: UUID): Unit =
      inTransaction { handle ->
        handle.createUpdate(sqlResource("store/feature/delete.sql"))
            .bind("featureGuid", featureGuid)
            .update() ?: throw FeatureNotFound()
      }

  private fun handleCreateError(e: UnableToExecuteStatementException): Nothing {
    val error = e.serverErrorMessage ?: throw e
    when {
      error.isForeignKeyViolation(FK_ORG_GUID) -> throw OrgNotFound().unprocessable()
      error.isUniqueConstraintViolation(UNIQ_NAME) -> throw FeatureNameIsNotUnique()
      error.isUniqueConstraintViolation(UNIQ_PATH) -> throw FeaturePathIsNotUnique()
      error.isUniqueConstraintViolation(UNIQ_RANK) -> throw FeatureRankIsNotUnique()
      else -> throw e
    }
  }

  private fun handleUpdateError(e: UnableToExecuteStatementException): Nothing {
    val error = e.serverErrorMessage ?: throw e
    when {
      error.isUniqueConstraintViolation(UNIQ_NAME) -> throw FeatureNameIsNotUnique()
      error.isUniqueConstraintViolation(UNIQ_PATH) -> throw FeaturePathIsNotUnique()
      error.isUniqueConstraintViolation(UNIQ_RANK) -> throw FeatureRankIsNotUnique()
      else -> throw e
    }
  }
}
