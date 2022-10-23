package limber.feature.rest

public sealed class RestImplementation {
  public object Local : RestImplementation()

  public data class Http(val baseUrl: String) : RestImplementation()
}
