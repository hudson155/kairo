package io.limberapp.backend.module.orgs.mapper.org

import com.google.inject.Inject
import com.piperframework.util.uuid.UuidGenerator
import io.limberapp.backend.module.orgs.model.org.FeatureModel
import io.limberapp.backend.module.orgs.model.org.OrgModel
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import java.time.Clock
import java.time.LocalDateTime

internal class OrgMapper @Inject constructor(
  private val clock: Clock,
  private val uuidGenerator: UuidGenerator,
  private val featureMapper: FeatureMapper
) {
  fun model(rep: OrgRep.Creation) = OrgModel(
    guid = uuidGenerator.generate(),
    createdDate = LocalDateTime.now(clock),
    name = rep.name,
    ownerAccountGuid = rep.ownerAccountGuid
  )

  fun completeRep(model: OrgModel, features: Set<FeatureModel>) = OrgRep.Complete(
    guid = model.guid,
    createdDate = model.createdDate,
    name = model.name,
    ownerAccountGuid = model.ownerAccountGuid,
    features = features.map { featureMapper.completeRep(it) }.toSet()
  )

  fun update(rep: OrgRep.Update) = OrgModel.Update(
    name = rep.name
  )
}
