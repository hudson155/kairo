package kairo.adminSample.author

import kotlin.time.Instant
import kotlinx.serialization.Serializable

@Serializable
public data class AuthorRep(
  val id: String,
  val createdAt: Instant,
  val updatedAt: Instant,
  val name: String,
  val bio: String?,
) {
  @Serializable
  public data class Creator(
    val name: String,
    val bio: String? = null,
  )

  @Serializable
  public data class Update(
    val name: String? = null,
    val bio: String? = null,
  )
}
