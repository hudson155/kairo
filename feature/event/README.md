# Event Feature

Adding `EventFeature` to a Server enables GCP Pub/Sub.

LIMITATIONS: This feature currently only supports publishing events, not consuming them.

## Usage

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
