package kairo.sql

import kairo.protectedString.ProtectedString
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

public data class SqlFeatureConfig(
  val connectionFactory: ConnectionFactory,
  val connectionPool: ConnectionPool = ConnectionPool(),
  val database: Database = Database(),
) {
  public data class ConnectionFactory(
    val url: String,
    val username: String? = null,
    val password: ProtectedString? = null,
    val ssl: Boolean? = null, // If null, uses driver's default.
    val connectTimeout: Duration = 4000.milliseconds,
    val statementTimeout: Duration = 10.seconds,
  )

  public data class ConnectionPool(
    val size: Size = Size(),
    val management: Management = Management(),
    val validation: Validation = Validation(),
  ) {
    public data class Size(
      val initial: Int = 10,
      val min: Int = 5,
      val max: Int = 25,
    )

    public data class Management(
      val createConnectionTimeout: Duration = 5000.milliseconds,
      val acquireTimeout: Duration = 1500.milliseconds,
      val acquireAttempts: Int = 3,
      val maxLifetime: Duration = 1.hours,
      val maxIdleTime: Duration = 5.minutes,
      val backgroundEvictionInterval: Duration = 2.minutes,
    )

    public data class Validation(
      val timeout: Duration = 250.milliseconds,
    )
  }

  public data class Database(
    val readOnly: Boolean = false,
    val defaultIsolationLevel: String? = null, // If null, uses driver's default.
    val maxAttempts: Int = 3,
  )
}
