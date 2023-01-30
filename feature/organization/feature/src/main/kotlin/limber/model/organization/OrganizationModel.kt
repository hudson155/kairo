package limber.model.organization

import java.time.ZonedDateTime
import java.util.UUID

public data class OrganizationModel(
  val guid: UUID,
  val version: Long,
  val createdAt: ZonedDateTime,
  val updatedAt: ZonedDateTime,
  val name: String,
) {
  public data class Creator(
    val guid: UUID,
    val name: String,
  )

  public data class Updater(
    val name: String?,
  )
}
