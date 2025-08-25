package kairo.libraryBook

import kairo.id.Id
import kotlinx.serialization.Serializable

@Serializable
@JvmInline
internal value class LibraryBookId(override val value: String) : Id {
  init {
    require(regex.matches(value)) { "Malformed library book ID (value=$value). " }
  }

  internal companion object {
    val regex: Regex = Id.regex(prefix = Regex("library_book"))
  }
}
