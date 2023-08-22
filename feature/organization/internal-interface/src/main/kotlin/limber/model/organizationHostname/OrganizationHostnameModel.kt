package limber.model.organizationHostname

import java.time.ZonedDateTime

public data class OrganizationHostnameModel(
  val id: String,
  val version: Long,
  val createdAt: ZonedDateTime,
  val updatedAt: ZonedDateTime,
  val organizationId: String,
  val hostname: String,
) {
  public data class Creator(
    val id: String,
    val organizationId: String,
    val hostname: String,
  )
}
