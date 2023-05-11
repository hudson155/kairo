package limber.model.organizationAuth

import java.time.ZonedDateTime
import java.util.UUID

public data class OrganizationAuthModel(
  val guid: UUID,
  val version: Long,
  val createdAt: ZonedDateTime,
  val updatedAt: ZonedDateTime,
  val organizationGuid: UUID,
  val auth0OrganizationId: String?,
  val auth0OrganizationName: String,
) {
  public data class Creator(
    val guid: UUID,
    val organizationGuid: UUID,
    val auth0OrganizationName: String,
  )

  public data class Update(
    val auth0OrganizationId: String?,
    val auth0OrganizationName: String,
  ) {
    internal constructor(auth: OrganizationAuthModel) : this(
      auth0OrganizationId = auth.auth0OrganizationId,
      auth0OrganizationName = auth.auth0OrganizationName,
    )
  }
}
