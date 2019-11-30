package io.limberapp.backend.module.orgs.mapper.app.org

import com.google.inject.Inject
import io.limberapp.backend.module.orgs.entity.org.OrgEntity
import io.limberapp.backend.module.orgs.mapper.app.feature.FeatureMapper
import io.limberapp.backend.module.orgs.mapper.app.membership.MembershipMapper
import io.limberapp.backend.module.orgs.model.org.OrgModel

internal class OrgMapper @Inject constructor(
    private val featureMapper: FeatureMapper,
    private val membershipMapper: MembershipMapper
) {

    fun entity(model: OrgModel) = OrgEntity(
        id = model.id,
        created = model.created,
        name = model.name,
        features = model.features.map { featureMapper.entity(it) },
        members = model.members.map { membershipMapper.entity(it) }
    )

    fun model(entity: OrgEntity) = OrgModel(
        id = entity.id,
        created = entity.created,
        name = entity.name,
        features = entity.features.map { featureMapper.model(it) },
        members = entity.members.map { membershipMapper.model(it) }
    )

    fun update(model: OrgModel.Update) = OrgEntity.Update(
        name = model.name
    )
}
