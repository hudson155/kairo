package kairo.adminSample.libraryBook.exception

import io.ktor.http.HttpStatusCode
import kairo.adminSample.libraryBook.LibraryBookId
import kairo.exception.LogicalFailure

public class LibraryBookNotFound(
  libraryBookId: LibraryBookId,
) : LogicalFailure("Library book not found (libraryBookId=${libraryBookId.value}).") {
  override val type: String = "LibraryBookNotFound"
  override val status: HttpStatusCode = HttpStatusCode.NotFound
}
