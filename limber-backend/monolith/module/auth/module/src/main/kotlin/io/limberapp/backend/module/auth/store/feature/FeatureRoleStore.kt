package io.limberapp.backend.module.auth.store.feature

import com.google.inject.Inject
import com.google.inject.Singleton
import io.limberapp.backend.module.auth.exception.feature.FeatureRoleNotFound
import io.limberapp.backend.module.auth.exception.feature.FeatureRoleOrgRoleIsNotUnique
import io.limberapp.backend.module.auth.model.feature.FeatureRoleFinder
import io.limberapp.backend.module.auth.model.feature.FeatureRoleModel
import io.limberapp.common.finder.Finder
import io.limberapp.common.store.SqlStore
import io.limberapp.common.store.isUniqueConstraintViolation
import io.limberapp.common.store.withFinder
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import java.util.*

private const val UNIQ_ORG_ROLE_GUID = "uniq__feature_role__org_role_guid"

@Singleton
internal class FeatureRoleStore @Inject constructor(
  jdbi: Jdbi,
) : SqlStore(jdbi), Finder<FeatureRoleModel, FeatureRoleFinder> {
  fun create(model: FeatureRoleModel): FeatureRoleModel =
    withHandle { handle ->
      return@withHandle try {
        handle.createQuery(sqlResource("/store/featureRole/create.sql"))
          .bindKotlin(model)
          .mapTo(FeatureRoleModel::class.java)
          .single()
      } catch (e: UnableToExecuteStatementException) {
        handleCreateError(e)
      }
    }

  override fun <R> find(result: (Iterable<FeatureRoleModel>) -> R, query: FeatureRoleFinder.() -> Unit): R =
    withHandle { handle ->
      handle.createQuery(sqlResource("/store/featureRole/find.sql"))
        .withFinder(FeatureRoleQueryBuilder().apply(query))
        .mapTo(FeatureRoleModel::class.java)
        .let(result)
    }

  fun update(featureGuid: UUID, featureRoleGuid: UUID, update: FeatureRoleModel.Update): FeatureRoleModel =
    inTransaction { handle ->
      return@inTransaction try {
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
      return@inTransaction handle.createUpdate(sqlResource("/store/featureRole/delete.sql"))
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
