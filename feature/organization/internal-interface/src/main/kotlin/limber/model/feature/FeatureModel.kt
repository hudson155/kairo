package limber.model.feature

import limber.rep.feature.FeatureRep
import java.time.ZonedDateTime

public data class FeatureModel(
  val id: String,
  val version: Long,
  val createdAt: ZonedDateTime,
  val updatedAt: ZonedDateTime,
  val organizationId: String,
  val isDefault: Boolean,
  val type: FeatureRep.Type,
  val name: String,
  val iconName: String?,
  val rootPath: String,
) {
  public data class Creator(
    val id: String,
    val organizationId: String,
    val isDefault: Boolean,
    val type: FeatureRep.Type,
    val name: String,
    val iconName: String?,
    val rootPath: String,
  )

  public data class Update(
    val isDefault: Boolean,
    val name: String,
    val iconName: String?,
    val rootPath: String,
  ) {
    public constructor(feature: FeatureModel) : this(
      isDefault = feature.isDefault,
      name = feature.name,
      iconName = feature.iconName,
      rootPath = feature.rootPath,
    )
  }
}
