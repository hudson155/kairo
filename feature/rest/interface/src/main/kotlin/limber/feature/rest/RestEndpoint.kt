package limber.feature.rest

import io.ktor.http.HttpMethod

public abstract class RestEndpoint<out T : Any> {
  public abstract val method: HttpMethod
  public abstract val path: String
  public open val qp: List<QueryParam> = emptyList()
  public open val body: T? = null
}
