package io.limberapp.backend.module.orgs.store.org

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import com.piperframework.store.isForeignKeyViolation
import com.piperframework.store.isUniqueConstraintViolation
import com.piperframework.util.uuid.singleNullOrThrow
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
import java.util.UUID

internal class SqlFeatureStore @Inject constructor(
    database: Database,
    private val sqlOrgMapper: SqlOrgMapper
) : FeatureStore, SqlStore(database) {

    override fun create(orgId: UUID, models: Set<FeatureModel>) = transaction {
        doOperation {
            FeatureTable.batchInsert(models) { model -> sqlOrgMapper.featureEntity(this, orgId, model) }
        } andHandleError {
            when {
                error.isForeignKeyViolation(FeatureTable.orgGuidForeignKey) -> throw OrgNotFound()
            }
        }
    }

    override fun create(orgId: UUID, model: FeatureModel) = transaction {
        FeatureTable
            .select { (FeatureTable.orgGuid eq orgId) and (FeatureTable.path eq model.path) }
            .singleNullOrThrow()?.let { throw FeatureIsNotUnique() }
        doOperation { FeatureTable.insert { sqlOrgMapper.featureEntity(it, orgId, model) } } andHandleError {
            when {
                error.isForeignKeyViolation(FeatureTable.orgGuidForeignKey) -> throw OrgNotFound()
            }
        }
    }

    override fun get(orgId: UUID, featureId: UUID) = transaction {
        val entity = FeatureTable
            .select { (FeatureTable.orgGuid eq orgId) and (FeatureTable.guid eq featureId) }
            .singleNullOrThrow() ?: return@transaction null
        return@transaction sqlOrgMapper.featureModel(entity)
    }

    override fun getByOrgId(orgId: UUID) = transaction {
        return@transaction FeatureTable
            .select { (FeatureTable.orgGuid eq orgId) }
            .map { sqlOrgMapper.featureModel(it) }
            .toSet()
    }

    override fun update(orgId: UUID, featureId: UUID, update: FeatureModel.Update) = transaction {
        doOperation {
            FeatureTable
                .updateExactlyOne(
                    where = { (FeatureTable.orgGuid eq orgId) and (FeatureTable.guid eq featureId) },
                    body = { sqlOrgMapper.featureEntity(it, update) },
                    notFound = { throw FeatureNotFound() }
                )
        } andHandleError {
            when {
                error.isUniqueConstraintViolation(FeatureTable.orgPathUniqueConstraint) -> throw FeatureIsNotUnique()
            }
        }
        return@transaction checkNotNull(get(orgId, featureId))
    }

    override fun delete(orgId: UUID, featureId: UUID) = transaction<Unit> {
        FeatureTable
            .deleteExactlyOne(
                where = { (FeatureTable.orgGuid eq orgId) and (FeatureTable.guid eq featureId) },
                notFound = { throw FeatureNotFound() }
            )
    }
}
