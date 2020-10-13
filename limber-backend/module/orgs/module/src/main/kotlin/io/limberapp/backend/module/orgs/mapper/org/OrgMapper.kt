package io.limberapp.backend.module.orgs.mapper.org

import com.google.inject.Inject
import io.limberapp.backend.module.orgs.mapper.feature.FeatureMapper
import io.limberapp.backend.module.orgs.model.feature.FeatureModel
import io.limberapp.backend.module.orgs.model.org.OrgModel
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.common.util.uuid.UuidGenerator
import java.time.Clock
import java.time.LocalDateTime

internal class OrgMapper @Inject constructor(
    private val clock: Clock,
    private val uuidGenerator: UuidGenerator,
    private val featureMapper: FeatureMapper,
) {
  fun model(rep: OrgRep.Creation) = OrgModel(
      guid = uuidGenerator.generate(),
      createdDate = LocalDateTime.now(clock),
      name = rep.name,
      ownerUserGuid = null,
  )

  fun completeRep(model: OrgModel, features: List<FeatureModel>) = OrgRep.Complete(
      guid = model.guid,
      createdDate = model.createdDate,
      name = model.name,
      ownerUserGuid = model.ownerUserGuid,
      features = features.map { featureMapper.completeRep(it) },
  )

  fun update(rep: OrgRep.Update) = OrgModel.Update(
      name = rep.name,
      ownerUserGuid = rep.ownerUserGuid,
  )
}
