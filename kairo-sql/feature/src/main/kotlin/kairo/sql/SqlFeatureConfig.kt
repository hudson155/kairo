package kairo.sql

import kairo.protectedString.ProtectedString
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
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
    val connectTimeout: Duration = 4000.milliseconds,
  )

  @Serializable
  public data class ConnectionPool(
    val size: Size = Size(),
  ) {
    @Serializable
    public data class Size(
      val min: Int = 5,
      val max: Int = 25,
    )
  }

  @Serializable
  public data class Database(
    val readOnly: Boolean = false,
    val maxAttempts: Int = 3,
  )
}
