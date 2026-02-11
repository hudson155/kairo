# GCP Pub/Sub

`kairo-gcp-pubsub` is a lightweight wrapper around
[Google Cloud Pub/Sub](https://cloud.google.com/pubsub),
making publishing and subscribing through Kotlin **idiomatic and coroutine-friendly**.
No more blocking calls on event loops!

```kotlin
val messageId = gcpPubSub.publish("my-topic", GcpPubSubMessage(data = "Hello, World!"))
```

## Installation

Install `kairo-gcp-pubsub-feature`.
You don't need to install the Pub/Sub SDK separately —
it's included by default.

```kotlin
// build.gradle.kts

dependencies {
  implementation("software.airborne.kairo:kairo-gcp-pubsub-feature")
  testImplementation("software.airborne.kairo:kairo-gcp-pubsub-testing")
}
```

## Usage

First, add the Feature to your Server.

```kotlin
val features = listOf(
  GcpPubSubFeature(config.gcpPubSub),
)
```

We recommend using [kairo-config](../kairo-config/README.md) to configure the Feature.

### Real (production)

```hocon
gcpPubSub {
  type = "Real"
  projectId = "my-gcp-project"
}
```

### Local (development)

```hocon
gcpPubSub {
  type = "Local"
  topicToSubscriptions {
    "my-topic" = ["my-subscription"]
  }
}
```

### Publishing

```kotlin
val gcpPubSub: GcpPubSub // Inject this.

val messageId = gcpPubSub.publish(
  topic = "my-topic",
  message = GcpPubSubMessage(
    data = """{"orderId": "123"}""",
    attributes = mapOf("eventType" to "order.created"),
  ),
)
```

### Subscribing

```kotlin
gcpPubSub.subscribe("my-subscription") { message ->
  println("Received: ${message.data}")
  println("Attributes: ${message.attributes}")
}
```

Messages are automatically ack'd after the handler completes successfully.
If the handler throws, the message is nack'd (and retried by Pub/Sub).

## Testing

Use alternative implementations to simplify testing.

- `NoopGcpPubSub`: Always throws `NotImplementedError`.
- `FakeGcpPubSub`: Stores published messages for assertions.
- Custom implementations: Implement `GcpPubSub`.

```kotlin
val fake = FakeGcpPubSub()
fake.publish("my-topic", GcpPubSubMessage(data = "test"))
val messages = fake.getPublishedMessages() // List<Pair<String, GcpPubSubMessage>>
fake.reset() // Clear stored messages between tests.
```

## Migration from direct Google Pub/Sub SDK

This section covers migrating from direct usage of
`com.google.cloud.pubsub.v1.Publisher` and `com.google.cloud.pubsub.v1.Subscriber`
to `kairo-gcp-pubsub`.

### Publishing

**Before:**

```kotlin
val topicName = TopicName.of("my-project", "my-topic")
val publisher = Publisher.newBuilder(topicName).build()
try {
  val message = PubsubMessage.newBuilder()
    .setData(ByteString.copyFromUtf8("""{"orderId": "123"}"""))
    .putAttributes("eventType", "order.created")
    .build()
  val messageId = publisher.publish(message).get()
} finally {
  publisher.shutdown()
}
```

**After:**

```kotlin
val gcpPubSub: GcpPubSub // Inject this.

val messageId = gcpPubSub.publish(
  topic = "my-topic",
  message = GcpPubSubMessage(
    data = """{"orderId": "123"}""",
    attributes = mapOf("eventType" to "order.created"),
  ),
)
```

**What changed:**
- Remove `TopicName`, `Publisher`, `PubsubMessage`, and `ByteString` imports.
- Replace `PubsubMessage.newBuilder()...build()` with `GcpPubSubMessage(...)`.
- Replace `publisher.publish(message).get()` with `gcpPubSub.publish(topic, message)`.
- Remove manual `Publisher` lifecycle management (`newBuilder`, `shutdown`).
  The Feature handles this automatically.

### Subscribing

**Before:**

```kotlin
val subscriptionName = ProjectSubscriptionName.of("my-project", "my-subscription")
val receiver = MessageReceiver { message, consumer ->
  try {
    val data = message.data.toStringUtf8()
    processMessage(data)
    consumer.ack()
  } catch (e: Exception) {
    consumer.nack()
  }
}
val subscriber = Subscriber.newBuilder(subscriptionName, receiver).build()
subscriber.startAsync().awaitRunning()
// ... later:
subscriber.stopAsync()
```

**After:**

```kotlin
val gcpPubSub: GcpPubSub // Inject this.

gcpPubSub.subscribe("my-subscription") { message ->
  processMessage(message.data)
}
```

**What changed:**
- Remove `ProjectSubscriptionName`, `Subscriber`, and `MessageReceiver` imports.
- Remove manual ack/nack handling. Messages are ack'd on success and nack'd on exception automatically.
- Remove `Subscriber` lifecycle management (`startAsync`, `stopAsync`).
  The Feature handles this automatically.

### Dependency injection

**Before:**

```kotlin
// Manual Publisher/Subscriber creation in each service.
class OrderService {
  private val publisher = Publisher.newBuilder(TopicName.of(projectId, "orders")).build()

  fun placeOrder(order: Order) {
    val message = PubsubMessage.newBuilder()
      .setData(ByteString.copyFromUtf8(json.writeValueAsString(order)))
      .build()
    publisher.publish(message).get()
  }
}
```

**After:**

```kotlin
// Single GcpPubSub instance injected via Koin.
class OrderService(
  private val gcpPubSub: GcpPubSub,
) {
  suspend fun placeOrder(order: Order) {
    gcpPubSub.publish(
      topic = "orders",
      message = GcpPubSubMessage(data = json.writeValueAsString(order)),
    )
  }
}
```

**What changed:**
- Replace per-service `Publisher` instances with a single injected `GcpPubSub`.
- For local development, swap configuration to `Local` — no code changes needed.

## Logging config

We recommend excluding logs below the `INFO` level for this library.

```xml
<Logger name="kairo.gcpPubSub" level="info"/>
```
