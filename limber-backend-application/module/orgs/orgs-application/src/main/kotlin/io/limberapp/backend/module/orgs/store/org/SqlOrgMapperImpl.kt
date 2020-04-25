package io.limberapp.backend.module.orgs.store.org

import com.google.inject.Inject
import io.limberapp.backend.module.orgs.entity.org.FeatureTable
import io.limberapp.backend.module.orgs.entity.org.OrgTable
import io.limberapp.backend.module.orgs.model.org.FeatureModel
import io.limberapp.backend.module.orgs.model.org.OrgModel
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement
import java.util.UUID

internal class SqlOrgMapperImpl @Inject constructor(
    private val featureStore: FeatureStore
) : SqlOrgMapper {
    override fun orgEntity(insertStatement: InsertStatement<*>, model: OrgModel) {
        insertStatement[OrgTable.createdDate] = model.created
        insertStatement[OrgTable.guid] = model.id
        insertStatement[OrgTable.name] = model.name
        insertStatement[OrgTable.ownerAccountGuid] = model.ownerAccountId
    }

    override fun orgEntity(updateStatement: UpdateStatement, update: OrgModel.Update) {
        update.name?.let { updateStatement[OrgTable.name] = it }
    }

    override fun featureEntity(insertStatement: InsertStatement<*>, orgId: UUID, model: FeatureModel) {
        insertStatement[FeatureTable.createdDate] = model.created
        insertStatement[FeatureTable.guid] = model.id
        insertStatement[FeatureTable.orgGuid] = orgId
        insertStatement[FeatureTable.name] = model.name
        insertStatement[FeatureTable.path] = model.path
        insertStatement[FeatureTable.type] = model.type.name
        insertStatement[FeatureTable.isDefaultFeature] = model.isDefaultFeature
    }

    override fun featureEntity(updateStatement: UpdateStatement, update: FeatureModel.Update) {
        update.name?.let { updateStatement[FeatureTable.name] = it }
        update.path?.let { updateStatement[FeatureTable.path] = it }
        update.isDefaultFeature?.let { updateStatement[FeatureTable.isDefaultFeature] = it }
    }

    override fun orgModel(resultRow: ResultRow): OrgModel {
        val guid = resultRow[OrgTable.guid]
        return OrgModel(
            id = guid,
            created = resultRow[OrgTable.createdDate],
            name = resultRow[OrgTable.name],
            ownerAccountId = resultRow[OrgTable.ownerAccountGuid],
            features = featureStore.getByOrgId(guid)
        )
    }

    override fun featureModel(resultRow: ResultRow) = FeatureModel(
        id = resultRow[FeatureTable.guid],
        created = resultRow[FeatureTable.createdDate],
        name = resultRow[FeatureTable.name],
        path = resultRow[FeatureTable.path],
        type = FeatureModel.Type.valueOf(resultRow[FeatureTable.type]),
        isDefaultFeature = resultRow[FeatureTable.isDefaultFeature]
    )
}
