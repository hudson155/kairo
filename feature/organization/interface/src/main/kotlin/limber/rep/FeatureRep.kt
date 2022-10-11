package limber.rep

import jakarta.validation.constraints.AssertTrue
import jakarta.validation.constraints.Pattern
import limber.validation.Regex
import java.util.UUID

public data class FeatureRep(
  val organizationGuid: UUID,
  val guid: UUID,
  val isDefault: Boolean,
  val type: Type,
  val rootPath: String,
) {
  public enum class Type { Placeholder, Forms }

  public data class Creator(
    val type: Type,
    @field:Pattern(regexp = Regex.Path.regex, message = Regex.Path.message) val rootPath: String,
  )

  public data class Updater(
    @field:AssertTrue val isDefault: Boolean? = null,
    @field:Pattern(regexp = Regex.Path.regex, message = Regex.Path.message) val rootPath: String? = null,
  )
}
