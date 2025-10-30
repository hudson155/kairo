package kairo.sql

import kairo.protectedString.ProtectedString
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlinx.serialization.Serializable

@Serializable
public data class SqlFeatureConfig(
  val connectionFactory: ConnectionFactory,
  val connectionPool: ConnectionPool = ConnectionPool(),
  val database: Database = Database(),
) {
  @Serializable
  public data class ConnectionFactory(
    val url: String,
    val username: String? = null,
    val password: ProtectedString? = null,
    val ssl: Boolean? = null, // If null, uses driver's default.
    val connectTimeout: Duration = 4000.milliseconds,
    val statementTimeout: Duration = 10.seconds,
  )

  @Serializable
  public data class ConnectionPool(
    val size: Size = Size(),
    val management: Management = Management(),
    val validation: Validation = Validation(),
  ) {
    @Serializable
    public data class Size(
      val initial: Int = 10,
      val min: Int = 5,
      val max: Int = 25,
    )

    @Serializable
    public data class Management(
      val createConnectionTimeout: Duration = 5000.milliseconds,
      val acquireTimeout: Duration = 1500.milliseconds,
      val acquireAttempts: Int = 3,
      val maxLifetime: Duration = 1.hours,
      val maxIdleTime: Duration = 5.minutes,
      val backgroundEvictionInterval: Duration = 2.minutes,
    )

    @Serializable
    public data class Validation(
      val timeout: Duration = 250.milliseconds,
    )
  }

  @Serializable
  public data class Database(
    val readOnly: Boolean = false,
    val defaultIsolationLevel: String? = null, // If null, uses driver's default.
    val maxAttempts: Int = 3,
  )
}
