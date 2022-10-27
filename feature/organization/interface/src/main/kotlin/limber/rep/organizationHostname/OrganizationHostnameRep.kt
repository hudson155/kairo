package limber.rep.organizationHostname

import jakarta.validation.constraints.Pattern
import limber.validation.HostnameValidator
import java.util.UUID

public data class OrganizationHostnameRep(
  val organizationGuid: UUID,
  val guid: UUID,
  val hostname: String,
) {
  public data class Creator(
    @field:Pattern(
      regexp = HostnameValidator.pattern,
      message = HostnameValidator.message,
    ) val hostname: String,
  )
}
