package kairo.restFeature

import com.fasterxml.jackson.annotation.JsonInclude
import kairo.id.KairoId

internal data class LibraryBookRep(
  val id: KairoId,
  val title: String,
  val author: String,
) {
  internal data class Creator(
    val title: String,
    val author: String,
  )

  @JsonInclude(value = JsonInclude.Include.NON_NULL)
  internal data class Update(
    val title: String? = null,
    val author: String? = null,
  )
}
