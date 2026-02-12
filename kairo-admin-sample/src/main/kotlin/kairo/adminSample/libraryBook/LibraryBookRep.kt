package kairo.adminSample.libraryBook

import kotlin.time.Instant
import kotlinx.serialization.Serializable

@Serializable
public data class LibraryBookRep(
  val id: String,
  val createdAt: Instant,
  val updatedAt: Instant,
  val title: String,
  val isbn: String,
  val genre: String,
) {
  @Serializable
  public data class Creator(
    val title: String,
    val isbn: String,
    val genre: String,
  )

  @Serializable
  public data class Update(
    val title: String? = null,
    val isbn: String? = null,
    val genre: String? = null,
  )
}
