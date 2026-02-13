package kairo.adminSample.libraryBook

import kairo.id.Id

@JvmInline
public value class LibraryBookId(override val value: String) : Id {
  public companion object : Id.Companion<LibraryBookId>() {
    override fun create(payload: String): LibraryBookId =
      LibraryBookId("book_$payload")
  }
}
