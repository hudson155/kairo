package limber.model.organizationAuth

import java.time.ZonedDateTime
import java.util.UUID

public data class OrganizationAuthModel(
  val guid: UUID,
  val version: Long,
  val createdAt: ZonedDateTime,
  val updatedAt: ZonedDateTime,
  val organizationGuid: UUID,
  val auth0OrganizationId: String,
) {
  public data class Creator(
    val guid: UUID,
    val organizationGuid: UUID,
    val auth0OrganizationId: String,
  )
}
