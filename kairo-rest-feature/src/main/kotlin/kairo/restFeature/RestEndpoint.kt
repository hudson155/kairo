package kairo.restFeature

import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

/**
 * A [RestEndpoint] implementation defines the API contract for a single REST API endpoint.
 * Implementations must be Kotlin data classes or data objects.
 * See this Feature's README or tests for some examples.
 *
 * [Request] represents the type of the request body. If none, use [Nothing].
 * [Response] represents the type of the response body. If none, use [Nothing].
 */
public abstract class RestEndpoint<Request : Any, Response : Any?> {
  /**
   * Mandatory: the [Method] must be one of [io.ktor.http.HttpMethod]; it must be all-caps.
   */
  @Target(AnnotationTarget.CLASS)
  public annotation class Method(val method: String)

  /**
   * Mandatory: the path should be something like "/library/library-books/:libraryBookId".
   */
  @Target(AnnotationTarget.CLASS)
  public annotation class Path(val path: String)

  /**
   * Use this to indicate path params.
   */
  @Target(AnnotationTarget.VALUE_PARAMETER)
  public annotation class PathParam

  /**
   * Use this to indicate query params.
   */
  @Target(AnnotationTarget.VALUE_PARAMETER)
  public annotation class QueryParam

  /**
   * Optional: Use this if (and only if) there's a request body.
   * The [ContentType] must be one of [io.ktor.http.ContentType].
   */
  @Target(AnnotationTarget.CLASS)
  public annotation class ContentType(val contentType: String)

  /**
   * Mandatory: the [Accept] must be one of [io.ktor.http.ContentType].
   */
  @Target(AnnotationTarget.CLASS)
  public annotation class Accept(val accept: String)

  /**
   * Although [body] is marked as nullable,
   * it must not be null unless [Request] is [Nothing].
   */
  public open val body: Request? = null
}

internal val KClass<out RestEndpoint<*, *>>.hasBody: Boolean
  get() {
    val body = memberProperties.single { it.name == RestEndpoint<*, *>::body.name }
    return body.returnType.classifier != Nothing::class
  }
