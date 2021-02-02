# Integration Testing

This library enables efficient integration testing for Limber features.
By leveraging this library,
integration tests can simply interact with the client classes
that their modules already create.
Behind the scenes, the client pipes these requests
directly into a Ktor `TestApplicationEngine`, bypassing the need for HTTP and REST.
This makes the tests a lot faster.

Additionally, this library is set up such that
the `TestApplicationEngine` doesn't need to be restarted for each test,
or even for each test class.
There's only one instance per feature.

### Implementation

Create the following class

```kotlin
@ExtendWith(IntegrationTest.Extension::class)
internal abstract class IntegrationTest(
    engine: TestApplicationEngine,
    server: Server<*>,
) : AbstractIntegrationTest(engine, server) {
  internal class Extension : AbstractIntegrationTestExtension() {
    override fun Application.main(): Server<TestConfig> = Server(
        application = this,
        modules = listOf(YourFeature()),
        config = ConfigLoader.load("test"),
    )
  }

  protected val yourClient: YourClient by lazy {
    YourClient(httpClient)
  }
}
```

Now you can write integration tests like this

```kotlin
internal class MyTest(
    engine: TestApplicationEngine,
    server: Server<*>,
) : IntegrationTest(engine, server)
```
