package kairo.adminSample.libraryBook

import kotlin.time.Instant

public data class LibraryBookModel(
  val id: LibraryBookId,
  val createdAt: Instant,
  val updatedAt: Instant,
  val title: String,
  val isbn: String,
  val genre: String,
) {
  public data class Creator(
    val title: String,
    val isbn: String,
    val genre: String,
  )

  public data class Update(
    val title: String? = null,
    val isbn: String? = null,
    val genre: String? = null,
  )
}
