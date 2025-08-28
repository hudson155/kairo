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
    val username: String,
    val password: ProtectedString,
    val ssl: Boolean? = null, // If null, uses driver's default.
    val connectTimeout: Duration = 4000.milliseconds,
    val statementTimeout: Duration = 10.seconds,
  )

  @Serializable
  public data class ConnectionPool(
    val acquireAttempts: Int = 3,
    val backgroundEvictionInterval: Duration = 2.minutes,
    val initialSize: Int = 10,
    val minIdle: Int = 5,
    val maxSize: Int = 25,
    val maxAcquireTime: Duration = 1500.milliseconds,
    val maxCreateConnectionTime: Duration = 5000.milliseconds,
    val maxIdleTime: Duration = 5.minutes,
    val maxLifeTime: Duration = 1.hours,
    val maxValidationTime: Duration = 250.milliseconds,
  )

  @Serializable
  public data class Database(
    val schema: String? = null,
    val defaultIsolationLevel: String? = null, // If null, uses driver's default.
    val readOnly: Boolean = false,
    val maxRetries: Int = 3,
  )
}
