package limber.rep.organization

import com.fasterxml.jackson.annotation.JsonInclude
import limber.validation.OrganizationNameValidator

public data class OrganizationRep(
  val id: String,
  val name: String,
) {
  public data class Creator(
    @OrganizationNameValidator val name: String,
  )

  @JsonInclude(value = JsonInclude.Include.NON_NULL)
  public data class Update(
    @OrganizationNameValidator val name: String? = null,
  )
}
