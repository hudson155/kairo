package kairo.adminSample.author

import kotlin.time.Instant

public data class AuthorModel(
  val id: AuthorId,
  val createdAt: Instant,
  val updatedAt: Instant,
  val name: String,
  val bio: String?,
) {
  public data class Creator(
    val name: String,
    val bio: String?,
  )

  public data class Update(
    val name: String? = null,
    val bio: String? = null,
  )
}
