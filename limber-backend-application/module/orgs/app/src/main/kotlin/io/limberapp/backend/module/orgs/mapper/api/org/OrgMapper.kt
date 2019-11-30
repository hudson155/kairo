package io.limberapp.backend.module.orgs.mapper.api.org

import com.google.common.annotations.VisibleForTesting
import com.google.inject.Inject
import com.piperframework.util.uuid.uuidGenerator.UuidGenerator
import io.limberapp.backend.module.orgs.mapper.api.feature.FeatureMapper
import io.limberapp.backend.module.orgs.mapper.api.membership.MembershipMapper
import io.limberapp.backend.module.orgs.model.org.FeatureModel
import io.limberapp.backend.module.orgs.model.org.OrgModel
import io.limberapp.backend.module.orgs.rep.feature.FeatureRep
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import java.time.Clock
import java.time.LocalDateTime

@VisibleForTesting
internal val DEFAULT_FEATURE_CREATION_REP = FeatureRep.Creation("Home", "/home", FeatureModel.Type.HOME)

internal class OrgMapper @Inject constructor(
    private val clock: Clock,
    private val uuidGenerator: UuidGenerator,
    private val featureMapper: FeatureMapper,
    private val membershipMapper: MembershipMapper
) {

    fun model(rep: OrgRep.Creation) = OrgModel(
        id = uuidGenerator.generate(),
        created = LocalDateTime.now(clock),
        name = rep.name,
        features = listOf(featureMapper.model(DEFAULT_FEATURE_CREATION_REP)),
        members = emptyList()
    )

    fun completeRep(model: OrgModel) = OrgRep.Complete(
        id = model.id,
        created = model.created,
        name = model.name,
        features = model.features.map { featureMapper.completeRep(it) },
        members = model.members.map { membershipMapper.completeRep(it) }
    )

    fun update(rep: OrgRep.Update) = OrgModel.Update(
        name = rep.name
    )
}
