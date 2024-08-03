package kairo.restFeature

public abstract class RestClient {
  public abstract suspend fun <Request, Response> request(
    endpoint: RestEndpoint<Request, Response>,
  ): Response
}
