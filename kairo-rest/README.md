# REST

Kairo is built on top of [Ktor](https://ktor.io/),
so it supports **all the features of Ktor**.

Kairo also provides an **advanced routing DSL**,
which is optional but highly recommended.

## Installation

Install `kairo-rest-feature`.
You don't need to install Ktor separately —
it's included by default.

```kotlin
// build.gradle.kts

dependencies {
  implementation("software.airborne.kairo:kairo-rest-feature")
}
```

## Usage

First, add the Feature to your Server.

```kotlin
val features = listOf(
  RestFeature(
    config = config.rest,
    authConfig = null, // This Server won't support auth. See below for an auth example.
  ),
)
```

We recommend using [kairo-config](../kairo-config/README.md) to configure the Feature.

```hocon
rest {
  connector.port = 8080
  plugins {
    defaultHeaders.serverName = "..."
  }
}
```

### The DSL

#### Define your endpoints

First, define your endpoints in a type-safe way.

```kotlin
@Serializable
data class UserRep(
  val id: UserId,
  val createdAt: Instant,
  val emailAddress: String,
) {
  @Serializable
  data class Creator(
    val emailAddress: String,
  )
}

object UserApi {
  @Rest("GET", "/users/:userId")
  @Rest.Accept("application/json")
  data class Get(
    @PathParam val userId: UserId,
  ) : RestEndpoint<Unit, UserRep>()

  @Rest("GET", "/users")
  @Rest.Accept("application/json")
  data class GetByEmailAddress(
    @QueryParam val emailAddress: String,
  ) : RestEndpoint<Unit, UserRep>()

  @Rest("GET", "/users")
  @Rest.Accept("application/json")
  data object ListAll : RestEndpoint<Unit, List<UserRep>>()

  @Rest("POST", "/users")
  @Rest.ContentType("application/json")
  @Rest.Accept("application/json")
  data class Create(
    override val body: UserRep.Creator,
  ) : RestEndpoint<UserRep.Creator, UserRep>()
}
```

#### Implement your handlers

```kotlin
@Single
class UserHandler(
  private val userMapper: UserMapper,
  private val userService: UserService,
) : HasRouting {
  @Routing
  override fun Application.routing() {
    routing {
      route(UserApi.Get::class) {
        handle {
          val user = userService.get(endpoint.userId)
            ?: throw UserNotFound(endpoint.userId)
          userMapper.rep(user)
        }
      }

      route(UserApi.GetByEmailAddress::class) {
        handle {
          val user = userService.getByEmailAddress(endpoint.emailAddress)
            ?: throw UserNotFound(null)
          userMapper.rep(user)
        }
      }

      route(UserApi.ListAll::class) {
        handle {
          val users = userService.listAll()
          users.map { userMapper.rep(it) }
        }
      }

      route(UserApi.Create::class) {
        handle {
          val user = userService.create(
            creator = userMapper.creator(endpoint.body),
          )
          userMapper.rep(user)
        }
      }
    }
  }
}
```

### Auth

If you want your Server to support auth, you **must** provide an `AuthConfig`.

```kotlin
class MyAuth : AuthConfig() {
  /**
   * Your implementation can be anything;
   * this shows you how to use JWK for JWT.
   */
  override fun AuthenticationConfig.configure() {
    val jwkProvider = JwkProviderBuilder(URI.create(config.jwkUrl).toURL()).build()
    jwt { credential ->
      val decoded = JWT.decode(credential.token)
      val jwk = jwkProvider.get(decoded.keyId ?: throw JwtVerificationFailed())
      val algorithm = Algorithm.RSA256(jwk.publicKey as RSAPublicKey, null)
      return@jwt JWT
        .require(algorithm)
        .withIssuer(config.issuer)
        .acceptLeeway(config.leeway.inWholeSeconds)
        .build()
    }
  }
}

RestFeature(
  config = config.rest,
  authConfig = MyAuth(),
)
```

## Advanced configuration

This section documents all available configuration options.

### `Parallelism`

The `Parallelism` config specifies how to manage thread pools.

- `runningLimit`: The maximum number of concurrent requests.
  Defaults to 50.
- `shareWorkGroup`: Whether to avoid creating a call group, sharing the worker group instead.
  Defaults to false.
- `connectionGroupSize`: How many threads for accepting new connections and starting call processing.
  Defaults to 1.
  If explicitly null, uses Netty's default.
- `workerGroupSize`: How many threads for processing connections, parsing messages and doing Netty's internal work.
  Defaults to 1.
  If explicitly null, uses Netty's default.
- `callGroupSize`: How many threads for processing application calls.
  Defaults to 10.
  If explicitly null, uses Netty's default.

### `Timeouts`

The `Timeouts` config specifies how timeouts work.

- `requestRead`: Defaults to 0, which is infinite.
- `responseWrite`: Defaults to 10 seconds.

### `Lifecycle`

The `Lifecycle` config specifies Server lifecycle.

- `shutdownGracePeriod`: The minimum amount of time to wait for cooldown.
  Defaults to 0.
- `shutdownTimeout`: The maximum amount of time to wait for cooldown.
  Defaults to 15 seconds.

### `Connector`

The `Connector` config specifies REST connectivity.

- `host`: Which host to listen on.
  Defaults to unmasked.
- `port`: The port to listen on.
  **No default** (required).

### `Plugins.AutoHeadResponse`

Configures Ktor's `AutoHeadResponse` plugin.

Defaults to null, which means the plugin is disabled.

### `Plugins.CallLogging`

Configures Ktor's `CallLogging` plugin.

Enabled by default. Disable by setting to null.

- `useColors`: Whether to use colors in the logs.
  Defaults to false.

### `Plugins.ContentNegotiation`

Configures Ktor's `ContentNegotiation` plugin.

Enabled by default. Disable by setting to null.

### `Plugins.Cors`

Configures Ktor's `Cors` plugin.

Defaults to null, which means the plugin is disabled.

- `hosts`: An array of allowed hosts.
- `hosts.host` The host to allow.
- `hosts.schemes`: The schemes to allow.
- `hosts.subdomains`: The subdomains to allow.
- `headers`: The headers to allow.
- `methods`: The methods to allow.
- `allowCredentials`: Whether to allow credentials.

### `Plugins.DefaultHeaders`

Configures Ktor's `DefaultHeaders` plugin.

Enabled by default. Disable by setting to null.

- `serverName`: The server name for the `Server` header.
  **No default** (required).
- `headers`: A map of default headers.

### `Plugins.DoubleReceive`

Configures Ktor's `DoubleReceive` plugin.

Defaults to null, which means the plugin is disabled.

- `cacheRawRequest`: Whether to cache the raw request.

### `Plugins.ForwardedHeaders`

Configures Ktor's `ForwardedHeaders` plugin.

Enabled by default. Disable by setting to null.

### `Plugins.Sse`

Configures Ktor's `Sse` plugin.

Defaults to null, which means the plugin is disabled.

## Logging config

We recommend excluding logs below the `INFO` level for this library.

```xml
<Logger name="kairo.rest" level="info"/>
```
