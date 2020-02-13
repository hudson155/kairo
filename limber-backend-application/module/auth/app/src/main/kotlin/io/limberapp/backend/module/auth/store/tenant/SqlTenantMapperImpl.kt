package io.limberapp.backend.module.auth.store.tenant

import com.google.inject.Inject
import io.limberapp.backend.module.auth.entity.tenant.TenantTable
import io.limberapp.backend.module.auth.model.tenant.TenantModel
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement

internal class SqlTenantMapperImpl @Inject constructor() : SqlTenantMapper {

    override fun tenantEntity(insertStatement: InsertStatement<*>, model: TenantModel) {
        insertStatement[TenantTable.createdDate] = model.created
        insertStatement[TenantTable.domain] = model.domain
        insertStatement[TenantTable.orgGuid] = model.orgId
        insertStatement[TenantTable.auth0ClientId] = model.auth0ClientId
    }

    override fun tenantEntity(updateStatement: UpdateStatement, update: TenantModel.Update) {
        update.domain?.let { updateStatement[TenantTable.domain] = it }
        update.orgId?.let { updateStatement[TenantTable.orgGuid] = it }
        update.auth0ClientId?.let { updateStatement[TenantTable.auth0ClientId] = it }
    }

    override fun tenantModel(resultRow: ResultRow) = TenantModel(
        domain = resultRow[TenantTable.domain],
        created = resultRow[TenantTable.createdDate],
        orgId = resultRow[TenantTable.orgGuid],
        auth0ClientId = resultRow[TenantTable.auth0ClientId]
    )
}
