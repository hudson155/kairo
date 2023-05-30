package limber.model.organizationHostname

import java.time.ZonedDateTime
import java.util.UUID

public data class OrganizationHostnameModel(
  val id: String,
  val version: Long,
  val createdAt: ZonedDateTime,
  val updatedAt: ZonedDateTime,
  val organizationGuid: UUID,
  val hostname: String,
) {
  public data class Creator(
    val id: String,
    val organizationGuid: UUID,
    val hostname: String,
  )
}
