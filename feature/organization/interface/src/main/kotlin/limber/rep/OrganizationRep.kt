package limber.rep

import java.util.UUID

public data class OrganizationRep(
  val guid: UUID,
  val name: String,
) {
  public data class Creator(val name: String)
}
