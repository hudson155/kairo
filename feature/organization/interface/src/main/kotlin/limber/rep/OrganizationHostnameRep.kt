package limber.rep

import jakarta.validation.constraints.Pattern
import limber.validation.Regex
import java.util.UUID

public data class OrganizationHostnameRep(
  val organizationGuid: UUID,
  val guid: UUID,
  val hostname: String,
) {
  public data class Creator(
    @field:Pattern(regexp = Regex.Hostname.regex, message = Regex.Hostname.message) val hostname: String,
  )
}
