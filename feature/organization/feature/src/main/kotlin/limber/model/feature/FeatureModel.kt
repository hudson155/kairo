package limber.model.feature

import limber.rep.feature.FeatureRep
import java.time.ZonedDateTime
import java.util.UUID

public data class FeatureModel(
  val guid: UUID,
  val version: Long,
  val createdAt: ZonedDateTime,
  val updatedAt: ZonedDateTime,
  val organizationGuid: UUID,
  val isDefault: Boolean,
  val type: FeatureRep.Type,
  val name: String,
  val iconName: String?,
  val rootPath: String,
) {
  public data class Creator(
    val guid: UUID,
    val organizationGuid: UUID,
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
    internal constructor(feature: FeatureModel) : this(
      isDefault = feature.isDefault,
      name = feature.name,
      iconName = feature.iconName,
      rootPath = feature.rootPath,
    )
  }
}
