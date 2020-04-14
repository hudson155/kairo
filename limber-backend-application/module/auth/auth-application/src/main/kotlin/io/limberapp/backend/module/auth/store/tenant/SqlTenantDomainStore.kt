package io.limberapp.backend.module.auth.store.tenant

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import com.piperframework.store.isForeignKeyViolation
import com.piperframework.store.isUniqueConstraintViolation
import io.limberapp.backend.module.auth.entity.tenant.TenantDomainTable
import io.limberapp.backend.module.auth.exception.tenant.TenantDomainAlreadyRegistered
import io.limberapp.backend.module.auth.exception.tenant.TenantDomainNotFound
import io.limberapp.backend.module.auth.exception.tenant.TenantNotFound
import io.limberapp.backend.module.auth.model.tenant.TenantDomainModel
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import java.util.UUID

internal class SqlTenantDomainStore @Inject constructor(
    database: Database,
    private val sqlTenantMapper: SqlTenantMapper
) : TenantDomainStore, SqlStore(database) {

    override fun create(orgId: UUID, models: Set<TenantDomainModel>) = transaction {
        doOperation {
            TenantDomainTable.batchInsert(models) { model -> sqlTenantMapper.tenantDomainEntity(this, orgId, model) }
        } andHandleError {
            when {
                error.isForeignKeyViolation(TenantDomainTable.orgGuidForeignKey) ->
                    throw TenantNotFound()
                error.isUniqueConstraintViolation(TenantDomainTable.domainUniqueConstraint) ->
                    throw TenantDomainAlreadyRegistered(models.singleOrNull()?.domain)
            }
        }
    }

    override fun create(orgId: UUID, model: TenantDomainModel) = transaction {
        doOperation {
            TenantDomainTable.insert { sqlTenantMapper.tenantDomainEntity(it, orgId, model) }
        } andHandleError {
            when {
                error.isForeignKeyViolation(TenantDomainTable.orgGuidForeignKey) ->
                    throw TenantNotFound()
                error.isUniqueConstraintViolation(TenantDomainTable.domainUniqueConstraint) ->
                    throw TenantDomainAlreadyRegistered(model.domain)
            }
        }
    }

    override fun getByOrgId(orgId: UUID) = transaction {
        return@transaction TenantDomainTable
            .select { (TenantDomainTable.orgGuid eq orgId) }
            .map { sqlTenantMapper.tenantDomainModel(it) }
            .toSet()
    }

    override fun delete(orgId: UUID, domain: String) = transaction<Unit> {
        TenantDomainTable
            .deleteExactlyOne(
                where = { (TenantDomainTable.orgGuid eq orgId) and (TenantDomainTable.domain eq domain) },
                notFound = { throw TenantDomainNotFound() }
            )
    }
}
