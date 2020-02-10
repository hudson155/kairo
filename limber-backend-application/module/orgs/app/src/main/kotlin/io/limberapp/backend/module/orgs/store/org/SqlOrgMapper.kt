package io.limberapp.backend.module.orgs.store.org

import io.limberapp.backend.module.orgs.model.org.FeatureModel
import io.limberapp.backend.module.orgs.model.org.MembershipModel
import io.limberapp.backend.module.orgs.model.org.OrgModel
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement
import java.util.UUID

interface SqlOrgMapper {

    fun featureEntity(insertStatement: InsertStatement<*>, orgId: UUID, model: FeatureModel)

    fun featureEntity(updateStatement: UpdateStatement, update: FeatureModel.Update)

    fun membershipEntity(insertStatement: InsertStatement<*>, orgId: UUID, model: MembershipModel)

    fun orgEntity(insertStatement: InsertStatement<*>, model: OrgModel)

    fun orgEntity(updateStatement: UpdateStatement, update: OrgModel.Update)

    fun featureModel(resultRow: ResultRow): FeatureModel

    fun membershipModel(resultRow: ResultRow): MembershipModel

    fun orgModel(resultRow: ResultRow): OrgModel
}
