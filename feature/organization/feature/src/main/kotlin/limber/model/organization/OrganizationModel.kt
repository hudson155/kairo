package limber.model.organization

import java.time.ZonedDateTime

public data class OrganizationModel(
  val id: String,
  val version: Long,
  val createdAt: ZonedDateTime,
  val updatedAt: ZonedDateTime,
  val name: String,
) {
  public data class Creator(
    val id: String,
    val name: String,
  )

  public data class Update(
    val name: String,
  ) {
    internal constructor(organization: OrganizationModel) : this(
      name = organization.name,
    )
  }
}
