package limber.feature.event

public abstract class EventPublisher<in T : Any> {
  public abstract class Factory {
    public abstract operator fun <T : Any> invoke(topicName: String): EventPublisher<T>

    internal abstract fun start()

    internal abstract fun stop()
  }

  public abstract fun publish(type: EventType, body: T)
}
