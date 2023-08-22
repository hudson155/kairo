package limber.model.organizationAuth

import java.time.ZonedDateTime

public data class OrganizationAuthModel(
  val id: String,
  val version: Long,
  val createdAt: ZonedDateTime,
  val updatedAt: ZonedDateTime,
  val organizationId: String,
  val auth0OrganizationId: String?,
  val auth0OrganizationName: String,
) {
  public data class Creator(
    val id: String,
    val organizationId: String,
    val auth0OrganizationName: String,
  )

  public data class Update(
    val auth0OrganizationId: String?,
    val auth0OrganizationName: String,
  ) {
    public constructor(auth: OrganizationAuthModel) : this(
      auth0OrganizationId = auth.auth0OrganizationId,
      auth0OrganizationName = auth.auth0OrganizationName,
    )
  }
}
