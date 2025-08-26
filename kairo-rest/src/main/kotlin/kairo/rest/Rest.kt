package kairo.rest

import kotlinx.serialization.MetaSerializable

/**
 * Annotations for [RestEndpoint] instances.
 */
@Target(AnnotationTarget.CLASS)
@MetaSerializable
public annotation class Rest(
  val method: String,
  /**
   * Format: "/library-books/:libraryBookId".
   */
  val path: String,
) {
  @Target(AnnotationTarget.CLASS)
  public annotation class ContentType(val value: String)

  @Target(AnnotationTarget.CLASS)
  public annotation class Accept(val value: String)
}
