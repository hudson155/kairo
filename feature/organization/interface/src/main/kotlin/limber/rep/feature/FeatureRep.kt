package limber.rep.feature

import com.fasterxml.jackson.annotation.JsonInclude
import limber.validation.FeatureNameValidator
import limber.validation.FeaturePathValidator
import limber.validation.IconNameValidator
import limber.validation.TrueValidator
import java.util.Optional
import java.util.UUID

public data class FeatureRep(
  val guid: UUID,
  val organizationGuid: UUID,
  val isDefault: Boolean,
  val type: Type,
  val name: String,
  val iconName: String?,
  val rootPath: String,
) {
  public enum class Type { Placeholder, Form }

  public data class Creator(
    val type: Type,
    @FeatureNameValidator val name: String,
    @IconNameValidator val iconName: String?,
    @FeaturePathValidator val rootPath: String,
  )

  @JsonInclude(value = JsonInclude.Include.NON_NULL)
  public data class Updater(
    @TrueValidator val isDefault: Boolean? = null,
    @FeatureNameValidator val name: String? = null,
    @IconNameValidator val iconName: Optional<String>? = null,
    @FeaturePathValidator val rootPath: String? = null,
  )
}
