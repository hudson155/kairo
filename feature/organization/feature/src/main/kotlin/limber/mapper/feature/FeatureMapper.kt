package limber.mapper.feature

import com.google.inject.Inject
import limber.model.feature.FeatureModel
import limber.rep.feature.FeatureRep
import limber.util.id.IdGenerator

internal class FeatureMapper @Inject constructor(
  idGenerator: IdGenerator.Factory,
) {
  private val idGenerator: IdGenerator = idGenerator("feat")

  operator fun invoke(feature: FeatureModel): FeatureRep =
    FeatureRep(
      id = feature.id,
      organizationId = feature.organizationId,
      isDefault = feature.isDefault,
      type = feature.type,
      name = feature.name,
      iconName = feature.iconName,
      rootPath = feature.rootPath,
    )

  operator fun invoke(
    organizationId: String,
    creator: FeatureRep.Creator,
  ): FeatureModel.Creator =
    FeatureModel.Creator(
      id = idGenerator.generate(),
      organizationId = organizationId,
      isDefault = false,
      type = creator.type,
      name = creator.name,
      iconName = creator.iconName,
      rootPath = creator.rootPath,
    )
}
