package kairo.adminSample.db

import com.typesafe.config.ConfigFactory
import kairo.hocon.deserialize
import kairo.serialization.KairoJson
import org.flywaydb.core.Flyway

public fun main() {
  val json = KairoJson()
  val hocon = ConfigFactory.parseResources("databaseConfig/local.conf").resolve()
  val config: DatabaseConfig = json.deserialize(hocon)

  val flyway = Flyway.configure()
    .dataSource(config.url, config.username, config.password)
    .locations("classpath:migration")
    .load()

  val result = flyway.migrate()
  println("Migrations applied: ${result.migrationsExecuted}")
}
