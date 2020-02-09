package io.limberapp.backend.module.orgs.store.org

import io.limberapp.backend.module.orgs.model.org.FeatureModel
import io.limberapp.backend.module.orgs.model.org.MembershipModel
import io.limberapp.backend.module.orgs.model.org.OrgModel
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement
import java.util.UUID

interface SqlOrgMapper {

    fun featureEntity(insertStatement: InsertStatement<*>, orgId: UUID, model: FeatureModel)

    fun membershipEntity(insertStatement: InsertStatement<*>, orgId: UUID, model: MembershipModel)

    fun orgEntity(insertStatement: InsertStatement<*>, model: OrgModel)

    fun featureModel(resultRow: ResultRow): FeatureModel

    fun membershipModel(resultRow: ResultRow): MembershipModel

    fun orgModel(resultRow: ResultRow): OrgModel
}
