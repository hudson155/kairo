package io.limberapp.backend.module.auth.store.tenant

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import com.piperframework.store.isUniqueConstraintViolation
import com.piperframework.util.singleNullOrThrow
import io.limberapp.backend.module.auth.exception.tenant.Auth0ClientIdAlreadyRegistered
import io.limberapp.backend.module.auth.exception.tenant.OrgAlreadyHasTenant
import io.limberapp.backend.module.auth.exception.tenant.TenantNotFound
import io.limberapp.backend.module.auth.model.tenant.TenantModel
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import org.jetbrains.exposed.sql.Database
import java.util.UUID

private const val ORG_GUID_UNIQUE_CONSTRAINT = "tenant_org_guid_key"
private const val AUTH0_CLIENT_ID_UNIQUE_CONSTRAINT = "tenant_auth0_client_id_key"

internal class TenantStore @Inject constructor(
    database: Database,
    private val jdbi: Jdbi,
    private val tenantDomainStore: TenantDomainStore
) : SqlStore(database) {
    fun create(model: TenantModel) {
        jdbi.useTransaction<Exception> {
            try {
                it.createUpdate(sqlResource("create")).bindKotlin(model).execute()
            } catch (e: UnableToExecuteStatementException) {
                val error = e.serverErrorMessage ?: throw e
                when {
                    error.isUniqueConstraintViolation(ORG_GUID_UNIQUE_CONSTRAINT) ->
                        throw OrgAlreadyHasTenant()
                    error.isUniqueConstraintViolation(AUTH0_CLIENT_ID_UNIQUE_CONSTRAINT) ->
                        throw Auth0ClientIdAlreadyRegistered()
                    else -> throw e
                }
            }
            tenantDomainStore.create(model.orgGuid, model.domains)
        }
    }

    fun get(orgGuid: UUID): TenantModel? {
        return jdbi.withHandle<TenantModel?, Exception> {
            it.createQuery("SELECT * FROM auth.tenant WHERE org_guid = :orgGuid")
                .bind("orgGuid", orgGuid)
                .mapTo(TenantModel::class.java)
                .singleNullOrThrow()
                ?.copy(domains = tenantDomainStore.getByOrgGuid(orgGuid))
        }
    }

    fun getByDomain(domain: String): TenantModel? {
        return jdbi.withHandle<TenantModel?, Exception> {
            val orgGuidSubquery = "(SELECT org_guid FROM auth.tenant_domain WHERE LOWER(domain) = LOWER(:domain))"
            it.createQuery("SELECT * FROM auth.tenant WHERE org_guid = $orgGuidSubquery")
                .bind("domain", domain)
                .mapTo(TenantModel::class.java)
                .singleNullOrThrow()
                ?.let { it.copy(domains = tenantDomainStore.getByOrgGuid(it.orgGuid)) }
        }
    }

    fun getByAuth0ClientId(auth0ClientId: String): TenantModel? {
        return jdbi.withHandle<TenantModel?, Exception> {
            it.createQuery("SELECT * FROM auth.tenant WHERE auth0_client_id = :auth0ClientId")
                .bind("auth0ClientId", auth0ClientId)
                .mapTo(TenantModel::class.java)
                .singleNullOrThrow()
                ?.let { it.copy(domains = tenantDomainStore.getByOrgGuid(it.orgGuid)) }
        }
    }

    fun update(orgGuid: UUID, update: TenantModel.Update): TenantModel {
        return jdbi.inTransaction<TenantModel, Exception> {
            val updateCount = try {
                it.createUpdate(sqlResource("update"))
                    .bind("orgGuid", orgGuid)
                    .bindKotlin(update)
                    .execute()
            } catch (e: UnableToExecuteStatementException) {
                val error = e.serverErrorMessage ?: throw e
                if (error.isUniqueConstraintViolation(AUTH0_CLIENT_ID_UNIQUE_CONSTRAINT)) {
                    throw Auth0ClientIdAlreadyRegistered()
                }
                throw e
            }
            when (updateCount) {
                0 -> throw TenantNotFound()
                1 -> return@inTransaction checkNotNull(get(orgGuid))
                else -> badSql()
            }
        }
    }

    fun delete(orgGuid: UUID) {
        jdbi.useTransaction<Exception> {
            val updateCount = it.createUpdate("DELETE FROM auth.tenant WHERE org_guid = :orgGuid")
                .bind("orgGuid", orgGuid)
                .execute()
            when (updateCount) {
                0 -> throw TenantNotFound()
                1 -> return@useTransaction
                else -> badSql()
            }
        }
    }
}
