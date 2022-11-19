package limber.rep.organizationHostname

import limber.validation.HostnameValidator
import java.util.UUID

public data class OrganizationHostnameRep(
  val organizationGuid: UUID,
  val guid: UUID,
  val hostname: String,
) {
  public data class Creator(
    @HostnameValidator val hostname: String,
  )
}
