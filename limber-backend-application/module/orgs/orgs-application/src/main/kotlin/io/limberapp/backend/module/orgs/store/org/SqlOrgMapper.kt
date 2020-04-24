package io.limberapp.backend.module.orgs.store.org

import io.limberapp.backend.module.orgs.model.org.FeatureModel
import io.limberapp.backend.module.orgs.model.org.OrgModel
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement
import java.util.UUID

internal interface SqlOrgMapper {
    fun orgEntity(insertStatement: InsertStatement<*>, model: OrgModel)

    fun orgEntity(updateStatement: UpdateStatement, update: OrgModel.Update)

    fun featureEntity(insertStatement: InsertStatement<*>, orgId: UUID, model: FeatureModel)

    fun featureEntity(updateStatement: UpdateStatement, update: FeatureModel.Update)

    fun orgModel(resultRow: ResultRow): OrgModel

    fun featureModel(resultRow: ResultRow): FeatureModel
}
