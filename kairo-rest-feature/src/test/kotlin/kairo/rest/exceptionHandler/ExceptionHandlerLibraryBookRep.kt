package kairo.rest.exceptionHandler

internal data class ExceptionHandlerLibraryBookRep(
  val title: String,
  val authors: List<Author>,
) {
  internal data class Creator(
    // Title is passed as a query param.
    val authors: List<Author>,
  )

  internal data class Author(
    val firstName: String,
    val lastName: String,
  )
}
