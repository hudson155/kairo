package kairo.restFeature

// TODO: Do we need Request and Response?
public abstract class RestHandler<Request, Response, Endpoint : RestEndpoint<*, *>> {
  @Suppress("UNCHECKED_CAST")
  public open val body: Request = null as Request
}
