package kairo.rest.exceptionHandler

/**
 * Part of [ExceptionHandlerTest].
 *
 * Allows tests to have the Server throw custom exceptions.
 */
internal abstract class LibraryBookService {
  abstract fun create(endpoint: ExceptionHandlerLibraryBookApi.Create): ExceptionHandlerLibraryBookRep
}
