package limber.mapper.feature

import com.google.inject.Inject
import limber.model.feature.FeatureModel
import limber.rep.feature.FeatureRep
import limber.util.guid.GuidGenerator
import java.util.UUID

internal class FeatureMapper @Inject constructor(
  private val guidGenerator: GuidGenerator,
) {
  operator fun invoke(feature: FeatureModel): FeatureRep =
    FeatureRep(
      guid = feature.guid,
      organizationGuid = feature.organizationGuid,
      isDefault = feature.isDefault,
      type = feature.type,
      name = feature.name,
      iconName = feature.iconName,
      rootPath = feature.rootPath,
    )

  operator fun invoke(
    organizationGuid: UUID,
    creator: FeatureRep.Creator,
  ): FeatureModel.Creator =
    FeatureModel.Creator(
      guid = guidGenerator.generate(),
      organizationGuid = organizationGuid,
      isDefault = false,
      type = creator.type,
      name = creator.name,
      iconName = creator.iconName,
      rootPath = creator.rootPath,
    )
}
