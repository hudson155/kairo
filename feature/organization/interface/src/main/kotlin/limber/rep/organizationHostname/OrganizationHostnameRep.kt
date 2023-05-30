package limber.rep.organizationHostname

import limber.validation.HostnameValidator

public data class OrganizationHostnameRep(
  val id: String,
  val organizationId: String,
  val hostname: String,
) {
  public data class Creator(
    @HostnameValidator val hostname: String,
  )
}
