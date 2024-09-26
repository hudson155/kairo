package kairo.rest.exceptionHandler

import com.google.inject.Inject
import kairo.rest.handler.RestHandler

@Suppress("UseDataClass") // Handlers shouldn't be data classes.
internal class ExceptionHandlerLibraryBookHandler @Inject constructor(
  private val libraryBookService: LibraryBookService,
) {
  internal inner class Create : RestHandler<ExceptionHandlerLibraryBookApi.Create, ExceptionHandlerLibraryBookRep>() {
    override suspend fun handle(endpoint: ExceptionHandlerLibraryBookApi.Create): ExceptionHandlerLibraryBookRep =
      libraryBookService.create(endpoint)
  }
}
