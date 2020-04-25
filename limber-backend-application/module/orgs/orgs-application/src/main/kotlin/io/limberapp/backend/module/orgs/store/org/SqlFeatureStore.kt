package io.limberapp.backend.module.orgs.store.org

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import com.piperframework.store.isForeignKeyViolation
import com.piperframework.store.isUniqueConstraintViolation
import com.piperframework.util.singleNullOrThrow
import io.limberapp.backend.module.orgs.entity.org.FeatureTable
import io.limberapp.backend.module.orgs.exception.org.FeatureIsNotUnique
import io.limberapp.backend.module.orgs.exception.org.FeatureNotFound
import io.limberapp.backend.module.orgs.exception.org.OrgNotFound
import io.limberapp.backend.module.orgs.model.org.FeatureModel
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update
import java.util.UUID

internal class SqlFeatureStore @Inject constructor(
    database: Database,
    private val sqlOrgMapper: SqlOrgMapper
) : FeatureStore, SqlStore(database) {
    override fun create(orgGuid: UUID, models: Set<FeatureModel>) = transaction {
        doOperation {
            FeatureTable.batchInsert(models) { model -> sqlOrgMapper.featureEntity(this, orgGuid, model) }
        } andHandleError {
            when {
                error.isForeignKeyViolation(FeatureTable.orgGuidForeignKey) -> throw OrgNotFound()
            }
        }
    }

    override fun create(orgGuid: UUID, model: FeatureModel) = transaction {
        FeatureTable
            .select { (FeatureTable.orgGuid eq orgGuid) and (FeatureTable.path eq model.path) }
            .singleNullOrThrow()?.let { throw FeatureIsNotUnique() }
        doOperation { FeatureTable.insert { sqlOrgMapper.featureEntity(it, orgGuid, model) } } andHandleError {
            when {
                error.isForeignKeyViolation(FeatureTable.orgGuidForeignKey) -> throw OrgNotFound()
            }
        }
    }

    override fun get(orgGuid: UUID, featureGuid: UUID) = transaction {
        val entity = FeatureTable
            .select { (FeatureTable.orgGuid eq orgGuid) and (FeatureTable.guid eq featureGuid) }
            .singleNullOrThrow() ?: return@transaction null
        return@transaction sqlOrgMapper.featureModel(entity)
    }

    override fun getByOrgGuid(orgGuid: UUID) = transaction {
        return@transaction FeatureTable
            .select { (FeatureTable.orgGuid eq orgGuid) }
            .map { sqlOrgMapper.featureModel(it) }
            .toSet()
    }

    override fun update(orgGuid: UUID, featureGuid: UUID, update: FeatureModel.Update) = transaction {
        if (update.isDefaultFeature == true) {
            FeatureTable
                .update(
                    where = { (FeatureTable.orgGuid eq orgGuid) and (FeatureTable.guid neq featureGuid) },
                    body = { it[isDefaultFeature] = false }
                )
        }

        doOperation {
            FeatureTable
                .updateExactlyOne(
                    where = { (FeatureTable.orgGuid eq orgGuid) and (FeatureTable.guid eq featureGuid) },
                    body = { sqlOrgMapper.featureEntity(it, update) },
                    notFound = { throw FeatureNotFound() }
                )
        } andHandleError {
            when {
                error.isUniqueConstraintViolation(FeatureTable.orgPathUniqueConstraint) -> throw FeatureIsNotUnique()
            }
        }
        return@transaction checkNotNull(get(orgGuid, featureGuid))
    }

    override fun delete(orgGuid: UUID, featureGuid: UUID) = transaction<Unit> {
        FeatureTable
            .deleteExactlyOne(
                where = { (FeatureTable.orgGuid eq orgGuid) and (FeatureTable.guid eq featureGuid) },
                notFound = { throw FeatureNotFound() }
            )
    }
}
