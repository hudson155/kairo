package kairo.libraryBook

import com.fasterxml.jackson.annotation.JsonInclude
import kairo.optional.Optional
import kairo.optional.Required
import kotlin.time.Instant

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

  internal data class Creator(
    val title: String? = null,
    val authors: List<String>,
    val isbn: String,
    val genre: Genre,
  )

  @JsonInclude(JsonInclude.Include.NON_ABSENT)
  internal data class Update(
    val title: Optional<String> = Optional.Missing,
    val authors: Required<List<String>> = Required.Missing,
    val genre: Required<Genre> = Required.Missing,
  )
}
