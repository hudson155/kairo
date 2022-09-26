package limber.rep

import jakarta.validation.constraints.Size
import java.util.UUID

public data class OrganizationRep(
  val guid: UUID,
  val name: String,
) {
  public data class Creator(
    @field:Size(min = 3, max = 255) val name: String,
  )
}
