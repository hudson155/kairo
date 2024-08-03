package kairo.restFeature

public abstract class RestEndpoint<Request, Response> {
  @Target(AnnotationTarget.CLASS)
  public annotation class Method(val method: String)

  @Target(AnnotationTarget.CLASS)
  public annotation class Path(val path: String)

  @Target(AnnotationTarget.VALUE_PARAMETER)
  public annotation class PathParam

  @Target(AnnotationTarget.VALUE_PARAMETER)
  public annotation class QueryParam

  @Suppress("UNCHECKED_CAST")
  public open val body: Request = null as Request
}
