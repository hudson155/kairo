# HTTP Client

`HttpClient` (uing `HttpClientImpl`) allows interaction with HTTP endpoints.
When constructing an HTTP client, it takes a base URL.
The intention is that an HTTP client is used for a single API,
so the base URL might be `https://api.example.com`.
This way, using the HTTP client does not require repeated passing of the base URL.
enables implementation of client classes
that interact with Limber service APIs.

Each Limber module generally implements a client for use by other services,
as well as by its own tests.

### Example

A simple example looks something like this.
```kotlin
object UserApi {
  data class Post(val rep: UserRep.Creation) : Endpoint(
      httpMethod = HttpMethod.Post,
      path = "/users",
      body = rep,
  )

  data class Get(val userId: Int) : Endpoint(
      httpMethod = HttpMethod.Get,
      path = "/users/${enc(userId)}",
  )

  data class GetByEmailAddress(val emailAddress: String) : Endpoint(
      httpMethod = HttpMethod.Get,
      path = "/users",
      queryParams = listOf("emailAddress" to enc(emailAddress)),
  )
}

class UserClient @Inject constructor(private val httpClient: HttpClient) {
  suspend operator fun invoke(
      endpoint: UserApi.Post,
      builder: (LimberHttpClientRequestBuilder.() -> Unit)? = null,
  ): UserRep.Complete =
      httpClient.request(endpoint, builder) { readValue(checkNotNull(it)) }

  suspend operator fun invoke(
      endpoint: UserApi.Get,
      builder: (LimberHttpClientRequestBuilder.() -> Unit)? = null,
  ): UserRep.Complete? =
      httpClient.request(endpoint, builder) { it?.let { readValue(it) } }

  suspend operator fun invoke(
      endpoint: UserApi.GetByEmailAddress,
      builder: (LimberHttpClientRequestBuilder.() -> Unit)? = null,
  ): UserRep.Complete? =
      httpClient.request(endpoint, builder) { it?.let { readValue(it) } }
}


val httpClient: HttpClient = HttpClientImpl("https://api.example.com", objectMapper)
val userClient: UserClient = UserClient(httpClient) // This would probably be injected in real code.
var user: UserRep.Complete = userClient(UserApi.GetByEmailAddress("jeff.hudson@limberapp.io"))
if (user == null) {
  user = userClient(UserApi.Post(emailAddress = "jeff.hudson@limberapp.io"))
}
```

### Testing

For testing, see `IntegrationTestHttpClient` in
the [integration testing library](/limber-backend/common/testing/integration).
