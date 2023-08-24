# Event Feature

Adding `EventFeature` to a Server enables GCP Pub/Sub.
Topics must be created manually, but subscriptions are created automatically.

## Usage

### Topic creation

Create topics using Terraform before referencing them in code.
Every topic should have a debug subscription created alongside it (also using Terraform).
Application subscriptions will be created automatically.

### Publishing

1. In your service, inject `EventPublisher.Factory`.
2. Get the relevant publisher.
3. On relevant mutation operations, **after database interaction**, publish events.

```kotlin
internal class CelebrityService @Inject constructor(
  private val celebrity: CelebrityStore,
  publisher: EventPublisher.Factory,
) {
  private val publisher: EventPublisher<CelebrityModel> = publisher("celebrity")

  fun get(id: String): CelebrityModel? =
    celebrityStore.get(id)

  fun create(creator: CelebrityModel.Creator): CelebrityModel {
    val celebrity = celebrityStore.create(creator)
    publisher.publish(EventType.Created, celebrity)
    return celebrity
  }

  fun update(id: String, updater: Updater<CelebrityModel.Update>): CelebrityModel {
    val celebrity = celebrityStore.update(id, updater)
    publisher.publish(EventType.Updated, celebrity)
    return celebrity
  }

  fun delete(id: String): CelebrityModel {
    val celebrity = celebrityStore.delete(id)
    publisher.publish(EventType.Deleted, celebrity)
    return celebrity
  }
}
```

### Subscribing

1. Create a new event handler.
2. Bind the event handler.

```kotlin
internal class CelebrityHandler @Inject constructor() : EventHandler<CelebrityModel>(
  subscriptionName = "default",
  topicName = "celebrity",
  kClass = CelebrityModel::class,
) {
  override fun handle(type: EventType, body: CelebrityModel) {
    ...
  }
}
```

```kotlin
binder.bindEventHandler(limber.event.default.CelebrityHandler::class)
```
