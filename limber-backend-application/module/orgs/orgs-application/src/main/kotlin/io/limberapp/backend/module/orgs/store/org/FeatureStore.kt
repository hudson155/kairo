package io.limberapp.backend.module.orgs.store.org

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import com.piperframework.store.isForeignKeyViolation
import com.piperframework.store.isUniqueConstraintViolation
import com.piperframework.util.singleNullOrThrow
import io.limberapp.backend.module.orgs.exception.org.FeatureNotFound
import io.limberapp.backend.module.orgs.exception.org.FeaturePathIsNotUnique
import io.limberapp.backend.module.orgs.exception.org.FeatureRankIsNotUnique
import io.limberapp.backend.module.orgs.exception.org.OrgNotFound
import io.limberapp.backend.module.orgs.model.org.FeatureModel
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import java.util.*

private const val ORG_GUID_FOREIGN_KEY = "feature_org_guid_fkey"
private const val PATH_UNIQUE_CONSTRAINT = "feature_org_guid_lower_idx"
private const val RANK_UNIQUE_CONSTRAINT = "feature_org_guid_rank_key"

internal class FeatureStore @Inject constructor(private val jdbi: Jdbi) : SqlStore() {
  fun create(model: FeatureModel): FeatureModel {
    return jdbi.withHandle<FeatureModel, Exception> {
      try {
        it.createQuery(sqlResource("create"))
          .bindKotlin(model)
          .mapTo(FeatureModel::class.java)
          .single()
      } catch (e: UnableToExecuteStatementException) {
        handleCreateError(e)
      }
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

  fun get(featureGuid: UUID): FeatureModel? {
    return jdbi.withHandle<FeatureModel?, Exception> {
      it.createQuery(
        """
        SELECT *
        FROM orgs.feature
        WHERE guid = :featureGuid
          AND archived_date IS NULL
        """.trimIndent()
      )
        .bind("featureGuid", featureGuid)
        .mapTo(FeatureModel::class.java)
        .singleNullOrThrow()
    }
  }

  fun getByOrgGuid(orgGuid: UUID): List<FeatureModel> {
    return jdbi.withHandle<List<FeatureModel>, Exception> {
      it.createQuery("SELECT * FROM orgs.feature WHERE org_guid = :orgGuid AND archived_date IS NULL ORDER BY rank")
        .bind("orgGuid", orgGuid)
        .mapTo(FeatureModel::class.java)
        .toList()
    }
  }

  fun update(orgGuid: UUID, featureGuid: UUID, update: FeatureModel.Update): FeatureModel {
    return jdbi.inTransaction<FeatureModel, Exception> {
      if (update.isDefaultFeature == true) {
        it.createUpdate(
          """
          UPDATE orgs.feature
          SET is_default_feature = FALSE
          WHERE org_guid = :orgGuid
            AND archived_date IS NULL
          """.trimIndent()
        )
          .bind("orgGuid", orgGuid)
          .execute()
      }
      val updateCount = try {
        it.createUpdate(sqlResource("update"))
          .bind("orgGuid", orgGuid)
          .bind("featureGuid", featureGuid)
          .bindKotlin(update)
          .execute()
      } catch (e: UnableToExecuteStatementException) {
        handleUpdateError(e)
      }
      return@inTransaction when (updateCount) {
        0 -> throw FeatureNotFound()
        1 -> checkNotNull(get(featureGuid))
        else -> badSql()
      }
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

  fun delete(featureGuid: UUID) {
    jdbi.useTransaction<Exception> {
      val updateCount = it.createUpdate(
        """
        UPDATE orgs.feature
        SET archived_date = NOW()
        WHERE guid = :featureGuid
          AND archived_date IS NULL
        """.trimIndent()
      )
        .bind("featureGuid", featureGuid)
        .execute()
      return@useTransaction when (updateCount) {
        0 -> throw FeatureNotFound()
        1 -> Unit
        else -> badSql()
      }
    }
  }
}
