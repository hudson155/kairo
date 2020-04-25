package io.limberapp.backend.module.auth.store.tenant

import com.google.inject.Inject
import io.limberapp.backend.module.auth.entity.tenant.TenantDomainTable
import io.limberapp.backend.module.auth.entity.tenant.TenantTable
import io.limberapp.backend.module.auth.model.tenant.TenantDomainModel
import io.limberapp.backend.module.auth.model.tenant.TenantModel
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement
import java.util.UUID

internal class SqlTenantMapperImpl @Inject constructor(
    private val tenantDomainStore: TenantDomainStore
) : SqlTenantMapper {
    override fun tenantEntity(insertStatement: InsertStatement<*>, model: TenantModel) {
        insertStatement[TenantTable.createdDate] = model.createdDate
        insertStatement[TenantTable.orgGuid] = model.orgGuid
        insertStatement[TenantTable.auth0ClientId] = model.auth0ClientId
    }

    override fun tenantEntity(updateStatement: UpdateStatement, update: TenantModel.Update) {
        update.auth0ClientId?.let { updateStatement[TenantTable.auth0ClientId] = it }
    }

    override fun tenantDomainEntity(insertStatement: InsertStatement<*>, orgGuid: UUID, model: TenantDomainModel) {
        insertStatement[TenantDomainTable.createdDate] = model.createdDate
        insertStatement[TenantDomainTable.orgGuid] = orgGuid
        insertStatement[TenantDomainTable.domain] = model.domain
    }

    override fun tenantModel(resultRow: ResultRow): TenantModel {
        val orgGuid = resultRow[TenantTable.orgGuid]
        return TenantModel(
            createdDate = resultRow[TenantTable.createdDate],
            orgGuid = orgGuid,
            auth0ClientId = resultRow[TenantTable.auth0ClientId],
            domains = tenantDomainStore.getByOrgGuid(orgGuid)
        )
    }

    override fun tenantDomainModel(resultRow: ResultRow) = TenantDomainModel(
        createdDate = resultRow[TenantDomainTable.createdDate],
        domain = resultRow[TenantDomainTable.domain]
    )
}
