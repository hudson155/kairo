package limber.feature.event

public abstract class EventPublisher<in T : Any> {
  public interface Factory {
    public operator fun <T : Any> invoke(topicName: String): EventPublisher<T>

    public fun close()
  }

  public abstract fun publish(type: EventType, body: T)
}
