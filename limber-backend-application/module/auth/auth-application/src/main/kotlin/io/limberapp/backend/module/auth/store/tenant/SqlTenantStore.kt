package io.limberapp.backend.module.auth.store.tenant

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import com.piperframework.store.isUniqueConstraintViolation
import com.piperframework.util.singleNullOrThrow
import io.limberapp.backend.module.auth.entity.tenant.TenantDomainTable
import io.limberapp.backend.module.auth.entity.tenant.TenantTable
import io.limberapp.backend.module.auth.exception.tenant.Auth0ClientIdAlreadyRegistered
import io.limberapp.backend.module.auth.exception.tenant.OrgAlreadyHasTenant
import io.limberapp.backend.module.auth.exception.tenant.TenantNotFound
import io.limberapp.backend.module.auth.model.tenant.TenantModel
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import java.util.UUID

internal class SqlTenantStore @Inject constructor(
    database: Database,
    private val tenantDomainStore: TenantDomainStore,
    private val sqlTenantMapper: SqlTenantMapper
) : TenantStore, SqlStore(database) {
    override fun create(model: TenantModel) = transaction {
        doOperation {
            TenantTable.insert { sqlTenantMapper.tenantEntity(it, model) }
        } andHandleError {
            when {
                error.isUniqueConstraintViolation(TenantTable.orgGuidUniqueConstraint) ->
                    throw OrgAlreadyHasTenant(model.orgGuid)
                error.isUniqueConstraintViolation(TenantTable.auth0ClientIdUniqueConstraint) ->
                    throw Auth0ClientIdAlreadyRegistered(model.auth0ClientId)
            }
        }
        tenantDomainStore.create(model.orgGuid, model.domains)
    }

    override fun get(orgGuid: UUID) = transaction {
        val entity = TenantTable
            .select { TenantTable.orgGuid eq orgGuid }
            .singleNullOrThrow() ?: return@transaction null
        return@transaction sqlTenantMapper.tenantModel(entity)
    }

    override fun getByDomain(domain: String) = transaction {
        val entity = (TenantTable innerJoin TenantDomainTable)
            .select { TenantDomainTable.domain eq domain }
            .singleNullOrThrow() ?: return@transaction null
        return@transaction sqlTenantMapper.tenantModel(entity)
    }

    override fun getByAuth0ClientId(auth0ClientId: String) = transaction {
        val entity = TenantTable
            .select { TenantTable.auth0ClientId eq auth0ClientId }
            .singleNullOrThrow() ?: return@transaction null
        return@transaction sqlTenantMapper.tenantModel(entity)
    }

    override fun update(orgGuid: UUID, update: TenantModel.Update) = transaction {
        doOperation {
            TenantTable
                .updateExactlyOne(
                    where = { TenantTable.orgGuid eq orgGuid },
                    body = { sqlTenantMapper.tenantEntity(it, update) },
                    notFound = { throw TenantNotFound() }
                )
        } andHandleError {
            when {
                error.isUniqueConstraintViolation(TenantTable.auth0ClientIdUniqueConstraint) ->
                    throw Auth0ClientIdAlreadyRegistered(checkNotNull(update.auth0ClientId))
            }
        }
        return@transaction checkNotNull(get(orgGuid))
    }

    override fun delete(orgGuid: UUID) = transaction<Unit> {
        TenantTable.deleteExactlyOne(
            where = { TenantTable.orgGuid eq orgGuid },
            notFound = { throw TenantNotFound() }
        )
    }
}
