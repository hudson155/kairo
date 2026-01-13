package kairo.rest

/**
 * Annotations for [RestEndpoint] instances.
 */
@Target(AnnotationTarget.CLASS)
public annotation class Rest(
  val method: String,
  val path: String,
) {
  @Target(AnnotationTarget.CLASS)
  public annotation class ContentType(val value: String)

  @Target(AnnotationTarget.CLASS)
  public annotation class Accept(val value: String)
}
