package io.limberapp.backend.module.auth.store.tenant

import io.limberapp.backend.module.auth.model.tenant.TenantModel
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement

interface SqlTenantMapper {

    fun tenantEntity(insertStatement: InsertStatement<*>, model: TenantModel)

    fun tenantEntity(updateStatement: UpdateStatement, update: TenantModel.Update)

    fun tenantModel(resultRow: ResultRow): TenantModel
}
