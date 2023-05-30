# REST Feature

Adding `RestFeature` to a Server allows for HTTP endpoints to be bound.
This is necessary for all Servers, even if the Server is not inherently RESTful
(such as Servers that handle asynchronous event processing)
because a health check GET endpoints are necessary.

## Usage

To expose a REST endpoint on the Server,
first implement `RestEndpoint` to define the endpoints' identities.
This goes in the interface module of a Feature.

```kotlin
public object CelebrityApi {
  public object List : RestEndpoint<Nothing>() {
    override val method: HttpMethod = HttpMethod.Get
    override val path: String = "/celebrities"
  }

  public data class Get(
    val celebrityId: String,
  ) : RestEndpoint<Nothing>() {
    override val method: HttpMethod = HttpMethod.Get
    override val path: String = "/celebrities/$celebrityId"
  }
}
```

Then implement `RestEndpointHandler` to define how the Server should handle requests.
This goes in the implementation module of the same Feature.

```kotlin
import CelebrityApi as Api
import CelebrityRep as Rep

public class ListCelebrities
@Inject constructor() : RestEndpointHandler<Api.List, List<Rep>>(Api.List::class) {
  override suspend fun handler(endpoint: Api.List): List<Rep> {
    // In real usage, this list would probably come from a database.
    return listOf(
      Rep(name = "Johnny Depp", age = 59),
      Rep(name = "Morgan Freeman", age = 85),
      Rep(name = "Taylor Swift", age = 32),
      Rep(name = "Tom Cruise", age = 60),
    )
  }
}

public class GetCelebrity
@Inject constructor() : RestEndpointHandler<Api.Get, Rep?>(Api.Get::class) {
  override suspend fun handler(endpoint: Api.Get): Rep? {
    // In real usage, this value would probably come from a database.
    return Rep(name = "Johnny Depp", age = 59)
  }
}
```

Now, bind these endpoint handlers in the respective Feature.

```kotlin
bindRestEndpoint(ListCelebrities::class)
bindRestEndpoint(GetCelebrity::class)
```

## Auth

Auth needs to be enabled through the `RestConfig`.

Authentication uses JWTs.
Authorization is implemented using JWT claims.

When auth is enabled, the JWT is available at any time during an endpoint handler's lifecycle,
but should usually only be checked within the endpoint handler directly.
Authorization can be checked using `auth()` and passing one of the `Auth` implementations.
Every endpoint must check authorization.

Custom `Auth` implementations should live within their respective Features.

## Implementation notes and limitations

[Ktor](https://ktor.io/) is used as the underlying Server implementation.

In order to tell Ktor what endpoint template to use
(what method, parameterized path, and other endpoint metadata to look for in request mapping),
an instance of the `RestEndpoint` implementation (`CelebrityApi` in the example above)
is created during Server startup, using randomized parameters.
**One important limitation of this approach is that
`RestEndpoint` implementations must plainly represent endpoints**.
They must not apply transformations to parameters,
nor implement `method` or `path` in any non-deterministic way.
