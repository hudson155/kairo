# Kairo Client

[Ktor-native](https://ktor.io/docs/client-requests.html)
outgoing HTTP requests from your Kairo application.

## Installation

Install `kairo-client-feature`.
You don't need to install Ktor dependencies separately —
they're included by default.

```kotlin
// build.gradle.kts

dependencies {
  implementation("software.airborne.kairo:kairo-client-feature")
}
```

## Usage

Create a separate Feature for each external integration.

```kotlin
class WeatherFeature : ClientFeature(httpClientName = "weather") {
  override val name: String = "Weather"

  override val timeout: Duration = 5.seconds

  override fun HttpClientConfig<*>.configure() {
    defaultRequest {
      url("https://api.weather.gov")
      userAgent("kairo (jeff@example.com)")
    }
  }
}
```

Inject and use the corresponding Ktor HTTP client.

```kotlin
@Named("weather") val weatherClient: HttpClient

val response = request {
  method = HttpMethod.Get
  url("/gridpoints/LWX/96,70/forecast")
}
return response.body()
```

### Advanced usage

Refer to the [Ktor client documentation](https://ktor.io/docs/client-requests.html)
for more advanced usage.
