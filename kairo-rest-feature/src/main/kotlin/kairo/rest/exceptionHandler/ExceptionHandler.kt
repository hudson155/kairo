package kairo.rest.exceptionHandler

/**
 * Exception handling is completely managed by Kairo.
 * Ktor's built-in exception handling is not used at all.
 *
 * An [ExceptionHandler] handles one specific type of exception.
 * See [ExceptionManager] for more information.
 */
internal abstract class ExceptionHandler {
  abstract fun handle(e: Throwable): ExceptionResult
}
