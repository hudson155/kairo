package limber.rep.organizationHostname

import limber.validation.HostnameValidator
import java.util.UUID

public data class OrganizationHostnameRep(
  val id: String,
  val organizationGuid: UUID,
  val hostname: String,
) {
  public data class Creator(
    @HostnameValidator val hostname: String,
  )
}
