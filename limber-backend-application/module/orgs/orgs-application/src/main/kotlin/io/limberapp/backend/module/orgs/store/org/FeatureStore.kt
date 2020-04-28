package io.limberapp.backend.module.orgs.store.org

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import com.piperframework.store.isForeignKeyViolation
import com.piperframework.store.isUniqueConstraintViolation
import com.piperframework.util.singleNullOrThrow
import io.limberapp.backend.module.orgs.exception.org.FeatureIsNotUnique
import io.limberapp.backend.module.orgs.exception.org.FeatureNotFound
import io.limberapp.backend.module.orgs.exception.org.OrgNotFound
import io.limberapp.backend.module.orgs.model.org.FeatureModel
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import java.util.UUID

private const val ORG_GUID_FOREIGN_KEY = "feature_org_guid_fkey"
private const val ORG_PATH_UNIQUE_CONSTRAINT = "feature_org_guid_lower_idx"

internal class FeatureStore @Inject constructor(private val jdbi: Jdbi) : SqlStore() {
    fun create(orgGuid: UUID, models: Set<FeatureModel>) {
        jdbi.useTransaction<Exception> {
            try {
                it.prepareBatch(sqlResource("create")).apply {
                    models.forEach { bind("orgGuid", orgGuid).bindKotlin(it).add() }
                }.execute()
            } catch (e: UnableToExecuteStatementException) {
                handleCreateError(e)
            }
        }
    }

    fun create(orgGuid: UUID, model: FeatureModel) {
        jdbi.useTransaction<Exception> {
            try {
                it.createUpdate(sqlResource("create")).bind("orgGuid", orgGuid).bindKotlin(model).execute()
            } catch (e: UnableToExecuteStatementException) {
                handleCreateError(e)
            }
        }
    }

    private fun handleCreateError(e: UnableToExecuteStatementException) {
        val error = e.serverErrorMessage ?: throw e
        when {
            error.isForeignKeyViolation(ORG_GUID_FOREIGN_KEY) -> throw OrgNotFound()
            error.isUniqueConstraintViolation(ORG_PATH_UNIQUE_CONSTRAINT) -> throw FeatureIsNotUnique()
            else -> throw e
        }
    }

    fun get(orgGuid: UUID, featureGuid: UUID): FeatureModel? {
        return jdbi.withHandle<FeatureModel?, Exception> {
            it.createQuery(
                    """
                    SELECT *
                    FROM orgs.feature
                    WHERE org_guid = :orgGuid
                      AND guid = :featureGuid
                      AND archived_date IS NULL
                    """.trimIndent()
                )
                .bind("orgGuid", orgGuid)
                .bind("featureGuid", featureGuid)
                .mapTo(FeatureModel::class.java)
                .singleNullOrThrow()
        }
    }

    fun getByOrgGuid(orgGuid: UUID): Set<FeatureModel> {
        return jdbi.withHandle<Set<FeatureModel>, Exception> {
            it.createQuery("SELECT * FROM orgs.feature WHERE org_guid = :orgGuid AND archived_date IS NULL")
                .bind("orgGuid", orgGuid)
                .mapTo(FeatureModel::class.java)
                .toSet()
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
            when (updateCount) {
                0 -> throw FeatureNotFound()
                1 -> return@inTransaction checkNotNull(get(orgGuid, featureGuid))
                else -> badSql()
            }
        }
    }

    private fun handleUpdateError(e: UnableToExecuteStatementException) {
        val error = e.serverErrorMessage ?: throw e
        if (error.isUniqueConstraintViolation(ORG_PATH_UNIQUE_CONSTRAINT)) throw FeatureIsNotUnique()
        throw e
    }

    fun delete(orgGuid: UUID, featureGuid: UUID) {
        jdbi.useTransaction<Exception> {
            val updateCount =
                it.createUpdate(
                        """
                        DELETE
                        FROM orgs.feature
                        WHERE org_guid = :orgGuid
                          AND guid = :featureGuid
                          AND archived_date IS NULL
                        """.trimIndent()
                    )
                    .bind("orgGuid", orgGuid)
                    .bind("featureGuid", featureGuid)
                    .execute()
            when (updateCount) {
                0 -> throw FeatureNotFound()
                1 -> return@useTransaction
                else -> badSql()
            }
        }
    }
}
