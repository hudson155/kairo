package limber.mapper.feature

import com.google.inject.Inject
import limber.model.feature.FeatureModel
import limber.rep.feature.FeatureRep
import limber.util.guid.GuidGenerator
import java.util.UUID

internal class FeatureMapper @Inject constructor(
  private val guidGenerator: GuidGenerator,
) {
  operator fun invoke(model: FeatureModel): FeatureRep =
    FeatureRep(
      guid = model.guid,
      organizationGuid = model.organizationGuid,
      isDefault = model.isDefault,
      type = model.type,
      name = model.name,
      iconName = model.iconName,
      rootPath = model.rootPath,
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
