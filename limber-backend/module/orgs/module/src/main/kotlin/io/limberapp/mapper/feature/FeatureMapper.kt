package io.limberapp.mapper.feature

import com.google.inject.Inject
import io.limberapp.model.feature.FeatureModel
import io.limberapp.rep.feature.FeatureRep
import io.limberapp.util.uuid.UuidGenerator
import java.time.Clock
import java.time.ZonedDateTime

internal class FeatureMapper @Inject constructor(
    private val clock: Clock,
    private val uuidGenerator: UuidGenerator,
) {
  fun model(rep: FeatureRep.Creation): FeatureModel =
      FeatureModel(
          guid = uuidGenerator.generate(),
          createdDate = ZonedDateTime.now(clock),
          orgGuid = rep.orgGuid,
          name = rep.name,
          path = rep.path.toLowerCase(),
          type = type(rep.type),
          rank = rep.rank,
          isDefaultFeature = false,
      )

  fun completeRep(model: FeatureModel): FeatureRep.Complete =
      FeatureRep.Complete(
          guid = model.guid,
          orgGuid = model.orgGuid,
          name = model.name,
          path = model.path,
          type = type(model.type),
          rank = model.rank,
          isDefaultFeature = model.isDefaultFeature,
      )

  fun update(rep: FeatureRep.Update): FeatureModel.Update =
      FeatureModel.Update(
          name = rep.name,
          path = rep.path?.toLowerCase(),
          rank = rep.rank,
          isDefaultFeature = rep.isDefaultFeature,
      )

  private fun type(type: FeatureRep.Type) =
      when (type) {
        FeatureRep.Type.FORMS -> FeatureModel.Type.FORMS
        FeatureRep.Type.HOME -> FeatureModel.Type.HOME
      }

  private fun type(type: FeatureModel.Type) =
      when (type) {
        FeatureModel.Type.FORMS -> FeatureRep.Type.FORMS
        FeatureModel.Type.HOME -> FeatureRep.Type.HOME
      }
}
