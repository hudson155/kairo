package limber.rep.organization

import com.fasterxml.jackson.annotation.JsonInclude
import limber.validation.OrganizationNameValidator
import java.util.UUID

public data class OrganizationRep(
  val guid: UUID,
  val name: String,
) {
  public data class Creator(
    @OrganizationNameValidator val name: String,
  )

  @JsonInclude(value = JsonInclude.Include.NON_NULL)
  public data class Updater(
    @OrganizationNameValidator val name: String? = null,
  )
}
