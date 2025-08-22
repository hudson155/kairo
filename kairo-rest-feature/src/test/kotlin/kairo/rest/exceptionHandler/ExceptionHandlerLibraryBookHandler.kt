package kairo.rest.exceptionHandler

import com.google.inject.Inject
import kairo.rest.auth.Auth
import kairo.rest.auth.AuthProvider
import kairo.rest.auth.public
import kairo.rest.handler.RestHandler

@Suppress("UseDataClass")
internal class ExceptionHandlerLibraryBookHandler @Inject constructor(
  private val libraryBookService: LibraryBookService,
) {
  internal inner class Create : RestHandler<ExceptionHandlerLibraryBookApi.Create, ExceptionHandlerLibraryBookRep>() {
    override suspend fun AuthProvider.auth(endpoint: ExceptionHandlerLibraryBookApi.Create): Auth.Result =
      public()

    override suspend fun handle(endpoint: ExceptionHandlerLibraryBookApi.Create): ExceptionHandlerLibraryBookRep =
      libraryBookService.create(endpoint)
  }
}
