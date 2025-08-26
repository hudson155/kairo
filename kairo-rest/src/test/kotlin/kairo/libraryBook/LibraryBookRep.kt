package kairo.libraryBook

import kotlin.time.Instant
import kotlinx.serialization.Serializable

@Serializable
internal data class LibraryBookRep(
  val id: LibraryBookId,
  val createdAt: Instant,
  val title: String?,
  val authors: List<String>,
  val isbn: String,
) {
  @Serializable
  internal data class Creator(
    val title: String?,
    val authors: List<String>,
    val isbn: String,
  )

  @Serializable
  // TODO: Support partial updates.
  internal data class Update(
    val title: String?,
    val authors: List<String>,
  )
}
