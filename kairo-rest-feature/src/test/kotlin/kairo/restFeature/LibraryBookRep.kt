package kairo.restFeature

import com.fasterxml.jackson.annotation.JsonInclude
import kairo.id.KairoId

internal data class LibraryBookRep(
  val id: KairoId,
  val title: String,
) {
  internal data class Creator(
    val title: String,
  )

  @JsonInclude(value = JsonInclude.Include.NON_NULL)
  internal data class Update(
    val title: String? = null,
  )
}
