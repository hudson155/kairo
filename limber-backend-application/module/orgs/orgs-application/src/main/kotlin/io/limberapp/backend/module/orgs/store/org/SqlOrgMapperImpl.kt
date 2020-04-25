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
        insertStatement[OrgTable.createdDate] = model.createdDate
        insertStatement[OrgTable.guid] = model.guid
        insertStatement[OrgTable.name] = model.name
        insertStatement[OrgTable.ownerAccountGuid] = model.ownerAccountGuid
    }

    override fun orgEntity(updateStatement: UpdateStatement, update: OrgModel.Update) {
        update.name?.let { updateStatement[OrgTable.name] = it }
    }

    override fun featureEntity(insertStatement: InsertStatement<*>, orgGuid: UUID, model: FeatureModel) {
        insertStatement[FeatureTable.createdDate] = model.createdDate
        insertStatement[FeatureTable.guid] = model.guid
        insertStatement[FeatureTable.orgGuid] = orgGuid
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
            guid = guid,
            createdDate = resultRow[OrgTable.createdDate],
            name = resultRow[OrgTable.name],
            ownerAccountGuid = resultRow[OrgTable.ownerAccountGuid],
            features = featureStore.getByOrgGuid(guid)
        )
    }

    override fun featureModel(resultRow: ResultRow) = FeatureModel(
        guid = resultRow[FeatureTable.guid],
        createdDate = resultRow[FeatureTable.createdDate],
        name = resultRow[FeatureTable.name],
        path = resultRow[FeatureTable.path],
        type = FeatureModel.Type.valueOf(resultRow[FeatureTable.type]),
        isDefaultFeature = resultRow[FeatureTable.isDefaultFeature]
    )
}
