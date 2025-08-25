package kairo.rest

import java.util.Optional
import kairo.id.Id

internal data class LibraryBookRep(
  val id: Id,
  val title: String,
  val author: String?,
  val isbn: String,
) {
  internal data class Creator(
    val title: String,
    val author: String?,
    val isbn: String,
  )

  // TODO: Support partial updates.
  internal data class Update(
    val title: String? = null,
    val author: Optional<String>? = null,
  )
}
