package io.limberapp.mapper.org

import com.google.inject.Inject
import io.limberapp.mapper.feature.FeatureMapper
import io.limberapp.model.feature.FeatureModel
import io.limberapp.model.org.OrgModel
import io.limberapp.rep.org.OrgRep
import io.limberapp.util.uuid.UuidGenerator
import java.time.Clock
import java.time.ZonedDateTime

internal class OrgMapper @Inject constructor(
    private val clock: Clock,
    private val uuidGenerator: UuidGenerator,
    private val featureMapper: FeatureMapper,
) {
  fun model(rep: OrgRep.Creation): OrgModel =
      OrgModel(
          guid = uuidGenerator.generate(),
          createdDate = ZonedDateTime.now(clock),
          name = rep.name,
          ownerUserGuid = null,
      )

  fun completeRep(model: OrgModel, features: List<FeatureModel>): OrgRep.Complete =
      OrgRep.Complete(
          guid = model.guid,
          name = model.name,
          ownerUserGuid = model.ownerUserGuid,
          features = features.map { featureMapper.completeRep(it) },
      )

  fun update(rep: OrgRep.Update): OrgModel.Update =
      OrgModel.Update(
          name = rep.name,
          ownerUserGuid = rep.ownerUserGuid,
      )
}
