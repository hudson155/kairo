package limber.api.gaeWarmup

import io.ktor.http.HttpMethod
import limber.feature.rest.RestEndpoint

public object GaeWarmupApi {
  public object Get : RestEndpoint<Nothing>() {
    override val method: HttpMethod = HttpMethod.Get
    override val path: String = "/_ah/warmup"
  }
}
