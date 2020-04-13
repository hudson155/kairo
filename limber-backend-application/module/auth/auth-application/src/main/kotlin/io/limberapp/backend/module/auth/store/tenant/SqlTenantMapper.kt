package io.limberapp.backend.module.auth.store.tenant

import io.limberapp.backend.module.auth.model.tenant.TenantDomainModel
import io.limberapp.backend.module.auth.model.tenant.TenantModel
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement
import java.util.UUID

internal interface SqlTenantMapper {

    fun tenantEntity(insertStatement: InsertStatement<*>, model: TenantModel)

    fun tenantEntity(updateStatement: UpdateStatement, update: TenantModel.Update)

    fun tenantDomainEntity(insertStatement: InsertStatement<*>, orgId: UUID, model: TenantDomainModel)

    fun tenantModel(resultRow: ResultRow): TenantModel

    fun tenantDomainModel(resultRow: ResultRow): TenantDomainModel
}
