package kairo.rest

/**
 * A [RestEndpoint] implementation defines the API contract for a single REST API endpoint.
 * Implementations must be Kotlin data classes or data objects.
 * See this Feature's README or tests for some examples.
 *
 * [I] (think: input) represents the type of the request body. If none, use [Unit].
 * [O] (think: output) represents the type of the response body. If none, use [Unit].
 */
public abstract class RestEndpoint<I : Any, O : Any> {
  @Target(AnnotationTarget.VALUE_PARAMETER)
  public annotation class PathParam

  @Target(AnnotationTarget.VALUE_PARAMETER)
  public annotation class QueryParam

  public open val body: I
    get() {
      throw NotImplementedError()
    }
}

// TODO: Log when endpoints are registered.

// TODO: Emit openai.yaml using KSP.
