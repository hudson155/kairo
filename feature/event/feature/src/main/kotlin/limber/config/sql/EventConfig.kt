package limber.config.sql

public data class EventConfig(
  val projectName: String,
  val publish: Publish? = null,
) {
  public data class Publish(
    val shutdownMs: Long,
  )
}
