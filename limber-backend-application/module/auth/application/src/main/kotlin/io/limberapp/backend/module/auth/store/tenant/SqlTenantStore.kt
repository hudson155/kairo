package io.limberapp.backend.module.auth.store.tenant

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import com.piperframework.store.isUniqueConstraintViolation
import com.piperframework.util.uuid.singleNullOrThrow
import io.limberapp.backend.module.auth.entity.tenant.TenantTable
import io.limberapp.backend.module.auth.exception.tenant.Auth0ClientIdAlreadyRegistered
import io.limberapp.backend.module.auth.exception.tenant.OrgAlreadyHasTenant
import io.limberapp.backend.module.auth.exception.tenant.TenantDomainAlreadyRegistered
import io.limberapp.backend.module.auth.exception.tenant.TenantNotFound
import io.limberapp.backend.module.auth.model.tenant.TenantModel
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.lowerCase
import org.jetbrains.exposed.sql.select

internal class SqlTenantStore @Inject constructor(
    database: Database,
    private val sqlTenantMapper: SqlTenantMapper
) : TenantStore, SqlStore(database) {

    override fun create(model: TenantModel) = transaction {
        doOperation {
            TenantTable.insert { sqlTenantMapper.tenantEntity(it, model) }
        } andHandleError {
            when {
                error.isUniqueConstraintViolation(TenantTable.domainUniqueConstraint) ->
                    throw TenantDomainAlreadyRegistered(model.domain)
                error.isUniqueConstraintViolation(TenantTable.orgGuidUniqueConstraint) ->
                    throw OrgAlreadyHasTenant(model.orgId)
                error.isUniqueConstraintViolation(TenantTable.auth0ClientIdUniqueConstraint) ->
                    throw Auth0ClientIdAlreadyRegistered(model.auth0ClientId)
            }
        }
    }

    override fun get(domain: String) = transaction {
        val entity = TenantTable
            .select { TenantTable.domain.lowerCase() eq domain.toLowerCase() }
            .singleNullOrThrow() ?: return@transaction null
        return@transaction sqlTenantMapper.tenantModel(entity)
    }

    override fun getByAuth0ClientId(auth0ClientId: String) = transaction {
        val entity = TenantTable
            .select { TenantTable.auth0ClientId eq auth0ClientId }
            .singleNullOrThrow() ?: return@transaction null
        return@transaction sqlTenantMapper.tenantModel(entity)
    }

    override fun update(domain: String, update: TenantModel.Update) = transaction {
        doOperation {
            TenantTable
                .updateExactlyOne(
                    where = { TenantTable.domain.lowerCase() eq domain.toLowerCase() },
                    body = { sqlTenantMapper.tenantEntity(it, update) },
                    notFound = { throw TenantNotFound() }
                )
        } andHandleError {
            when {
                error.isUniqueConstraintViolation(TenantTable.domainUniqueConstraint) ->
                    throw TenantDomainAlreadyRegistered(checkNotNull(update.domain))
                error.isUniqueConstraintViolation(TenantTable.orgGuidUniqueConstraint) ->
                    throw OrgAlreadyHasTenant(checkNotNull(update.orgId))
                error.isUniqueConstraintViolation(TenantTable.auth0ClientIdUniqueConstraint) ->
                    throw Auth0ClientIdAlreadyRegistered(checkNotNull(update.auth0ClientId))
            }
        }
        return@transaction checkNotNull(get(update.domain ?: domain))
    }

    override fun delete(domain: String) = transaction<Unit> {
        TenantTable.deleteExactlyOne(
            where = { TenantTable.domain.lowerCase() eq domain.toLowerCase() },
            notFound = { throw TenantNotFound() }
        )
    }
}
