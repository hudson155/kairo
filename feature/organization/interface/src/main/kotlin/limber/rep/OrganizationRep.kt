package limber.rep

import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import limber.validation.Regex
import java.util.UUID

public data class OrganizationRep(
  val guid: UUID,
  val name: String,
  val hostnames: List<String>,
) {
  public data class Creator(
    @field:Size(min = 3, max = 255) val name: String,
    @field:Pattern(regexp = Regex.Hostname.regex, message = Regex.Hostname.message) val hostname: String? = null,
  )

  public data class Updater(
    @field:Size(min = 3, max = 255) val name: String? = null,
  )
}
