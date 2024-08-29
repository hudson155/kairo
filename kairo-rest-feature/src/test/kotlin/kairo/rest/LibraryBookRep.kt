package kairo.rest

import com.fasterxml.jackson.annotation.JsonInclude
import java.util.Optional
import kairo.id.KairoId

internal data class LibraryBookRep(
  val id: KairoId,
  val title: String,
  val author: String?,
  val isbn: String,
) {
  internal data class Creator(
    val title: String,
    val author: String?,
    val isbn: String,
  )

  @JsonInclude(value = JsonInclude.Include.NON_NULL)
  internal data class Update(
    val title: String? = null,
    val author: Optional<String>? = null,
  )
}
