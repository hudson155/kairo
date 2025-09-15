package kairo.libraryBook

import kairo.optional.Optional
import kotlin.time.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Serializable

@Serializable
internal data class LibraryBookRep(
  val id: LibraryBookId,
  val createdAt: Instant,
  val title: String?,
  val authors: List<String>,
  val isbn: String,
  val genre: Genre,
) {
  internal enum class Genre {
    Fantasy,
    History,
    Religion,
    Romance,
    Science,
    ScienceFiction,
  }

  @Serializable
  internal data class Creator(
    val title: String?,
    val authors: List<String>,
    val isbn: String,
    val genre: Genre,
  )

  @Serializable
  internal data class Update(
    @EncodeDefault(EncodeDefault.Mode.NEVER) @Contextual
    val title: Optional<String> = Optional.Missing,
    val authors: List<String>? = null, // TODO: Use Required<T>.
    val genre: Genre? = null, // TODO: Use Required<T>.
  )
}
