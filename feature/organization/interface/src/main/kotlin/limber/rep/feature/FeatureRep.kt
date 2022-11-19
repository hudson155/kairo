package limber.rep.feature

import com.fasterxml.jackson.annotation.JsonInclude
import limber.validation.FeatureNameValidator
import limber.validation.FeaturePathValidator
import limber.validation.TrueValidator
import java.util.UUID

public data class FeatureRep(
  val organizationGuid: UUID,
  val guid: UUID,
  val isDefault: Boolean,
  val type: Type,
  val name: String,
  val rootPath: String,
) {
  public enum class Type { Placeholder, Form }

  public data class Creator(
    val type: Type,
    @FeatureNameValidator val name: String,
    @FeaturePathValidator val rootPath: String,
  )

  @JsonInclude(value = JsonInclude.Include.NON_NULL)
  public data class Updater(
    @TrueValidator val isDefault: Boolean? = null,
    @FeatureNameValidator val name: String? = null,
    @FeaturePathValidator val rootPath: String? = null,
  )
}
