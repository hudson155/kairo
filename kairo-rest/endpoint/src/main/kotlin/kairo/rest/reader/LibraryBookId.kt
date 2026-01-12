package kairo.rest.reader

import kairo.id.Id

@JvmInline
internal value class LibraryBookId(override val value: String) : Id {
  init {
    require(regex.matches(value)) { "Malformed library book ID (value=$value). " }
  }

  internal companion object : Id.Companion<LibraryBookId>() {
    val regex: Regex = regex(prefix = Regex("library_book"))

    override fun create(payload: String): LibraryBookId =
      LibraryBookId("library_book_$payload")
  }
}
