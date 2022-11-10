package limber.rep.feature

import jakarta.validation.constraints.AssertTrue
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import limber.validation.FeaturePathValidator
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
    @field:Size(min = 3, max = 31) val name: String,
    @field:Pattern(
      regexp = FeaturePathValidator.pattern,
      message = FeaturePathValidator.message,
    ) val rootPath: String,
  )

  public data class Updater(
    @field:AssertTrue val isDefault: Boolean? = null,
    @field:Size(min = 3, max = 31) val name: String? = null,
    @field:Pattern(
      regexp = FeaturePathValidator.pattern,
      message = FeaturePathValidator.message,
    ) val rootPath: String? = null,
  )
}
