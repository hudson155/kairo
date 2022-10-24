package limber.rep.feature

import jakarta.validation.constraints.AssertTrue
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import limber.validation.Regex
import java.util.UUID

public data class FeatureRep(
  val organizationGuid: UUID,
  val guid: UUID,
  val isDefault: Boolean,
  val type: Type,
  val name: String,
  val rootPath: String,
) {
  public enum class Type { Placeholder, Forms }

  public data class Creator(
    val type: Type,
    @field:Size(min = 3, max = 31) val name: String,
    @field:Pattern(regexp = Regex.Path.regex, message = Regex.Path.message) val rootPath: String,
  )

  public data class Updater(
    @field:AssertTrue val isDefault: Boolean? = null,
    @field:Size(min = 3, max = 31) val name: String? = null,
    @field:Pattern(regexp = Regex.Path.regex, message = Regex.Path.message) val rootPath: String? = null,
  )
}
