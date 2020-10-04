package io.limberapp.backend.module.orgs.store.feature

import com.google.inject.Inject
import com.google.inject.Singleton
import io.limberapp.backend.module.orgs.exception.feature.FeatureNotFound
import io.limberapp.backend.module.orgs.exception.feature.FeaturePathIsNotUnique
import io.limberapp.backend.module.orgs.exception.feature.FeatureRankIsNotUnique
import io.limberapp.backend.module.orgs.exception.org.OrgNotFound
import io.limberapp.backend.module.orgs.model.org.FeatureFinder
import io.limberapp.backend.module.orgs.model.org.FeatureModel
import io.limberapp.common.finder.Finder
import io.limberapp.common.store.SqlStore
import io.limberapp.common.store.isForeignKeyViolation
import io.limberapp.common.store.isUniqueConstraintViolation
import io.limberapp.common.store.withFinder
import io.limberapp.exception.unprocessableEntity.unprocessable
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import java.util.*

private const val FK_ORG_GUID = "fk__feature__org_guid"
private const val UNIQ_PATH = "uniq__feature__path"
private const val UNIQ_RANK = "uniq__feature__rank"

@Singleton
internal class FeatureStore @Inject constructor(jdbi: Jdbi) : SqlStore(jdbi), Finder<FeatureModel, FeatureFinder> {
  fun create(model: FeatureModel): FeatureModel =
    withHandle { handle ->
      try {
        handle.createQuery(sqlResource("/store/feature/create.sql"))
          .bindKotlin(model)
          .mapTo(FeatureModel::class.java)
          .single()
      } catch (e: UnableToExecuteStatementException) {
        handleCreateError(e)
      }
    }

  override fun <R> find(result: (Iterable<FeatureModel>) -> R, query: FeatureFinder.() -> Unit): R =
    withHandle { handle ->
      handle.createQuery(sqlResource("/store/feature/find.sql"))
        .withFinder(FeatureQueryBuilder().apply(query))
        .mapTo(FeatureModel::class.java)
        .let(result)
    }

  fun update(orgGuid: UUID, featureGuid: UUID, update: FeatureModel.Update): FeatureModel =
    inTransaction { handle ->
      if (update.isDefaultFeature == true) {
        handle.createUpdate(sqlResource("/store/feature/removeDefaultFeaturesByOrgGuid.sql"))
          .bind("orgGuid", orgGuid)
          .execute()
      }
      try {
        handle.createQuery(sqlResource("/store/feature/update.sql"))
          .bind("orgGuid", orgGuid)
          .bind("featureGuid", featureGuid)
          .bindKotlin(update)
          .mapTo(FeatureModel::class.java)
          .singleNullOrThrow()
      } catch (e: UnableToExecuteStatementException) {
        handleUpdateError(e)
      } ?: throw FeatureNotFound()
    }

  fun delete(orgGuid: UUID, featureGuid: UUID): Unit =
    inTransaction { handle ->
      handle.createUpdate(sqlResource("/store/feature/delete.sql"))
        .bind("orgGuid", orgGuid)
        .bind("featureGuid", featureGuid)
        .updateOnly() ?: throw FeatureNotFound()
    }

  private fun handleCreateError(e: UnableToExecuteStatementException): Nothing {
    val error = e.serverErrorMessage ?: throw e
    when {
      error.isForeignKeyViolation(FK_ORG_GUID) -> throw OrgNotFound().unprocessable()
      error.isUniqueConstraintViolation(UNIQ_PATH) -> throw FeaturePathIsNotUnique()
      error.isUniqueConstraintViolation(UNIQ_RANK) -> throw FeatureRankIsNotUnique()
      else -> throw e
    }
  }

  private fun handleUpdateError(e: UnableToExecuteStatementException): Nothing {
    val error = e.serverErrorMessage ?: throw e
    when {
      error.isUniqueConstraintViolation(UNIQ_PATH) -> throw FeaturePathIsNotUnique()
      error.isUniqueConstraintViolation(UNIQ_RANK) -> throw FeatureRankIsNotUnique()
      else -> throw e
    }
  }
}
