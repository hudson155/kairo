package limber.config.event

public data class EventConfig(
  val projectName: String,
  val publish: Publish? = null,
  val subscribe: Subscribe? = null,
  val transactionAware: Boolean,
) {
  public data class Publish(
    val shutdownMs: Long,
  )

  public data class Subscribe(
    val startupMs: Long,
    val shutdownMs: Long,
    val maxOutstandingElements: Long,
    val maxOutstandingBytes: Long,
    val minimumBackoffMs: Long,
    val maximumBackoffMs: Long,
  )
}
