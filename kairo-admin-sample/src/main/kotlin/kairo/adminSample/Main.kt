package kairo.adminSample

import kairo.admin.AdminDashboardConfig
import kairo.admin.AdminDashboardFeature
import kairo.adminSample.author.AuthorFeature
import kairo.adminSample.libraryBook.LibraryBookFeature
import kairo.application.kairo
import kairo.config.loadConfig
import kairo.dependencyInjection.DependencyInjectionFeature
import kairo.healthCheck.HealthCheckFeature
import kairo.kdocs.KdocsFeature
import kairo.rest.RestFeature
import kairo.serialization.KairoJson
import kairo.server.Server
import kairo.sql.SqlFeature
import org.koin.dsl.koinApplication

public fun main(): Unit = kairo {
  val json = KairoJson()
  val config: Config = loadConfig(json = json)
  val koinApplication = koinApplication()
  val koin = koinApplication.koin

  val server = Server(
    name = "Kairo Admin Sample",
    features = listOf(
      DependencyInjectionFeature(koinApplication),
      RestFeature(
        config = config.rest,
        authConfig = null,
        json = json,
      ),
      SqlFeature(
        config = config.sql,
        configureDatabase = { setUrl(config.sql.connectionFactory.url) },
      ),
      HealthCheckFeature(),
      LibraryBookFeature(koin),
      AuthorFeature(koin),
      KdocsFeature(),
      AdminDashboardFeature(
        config = AdminDashboardConfig(
          serverName = "Kairo Admin Sample",
          docsUrl = "https://github.com/hudson155/kairo/tree/main/kairo-admin-sample",
          apiDocsUrl = "https://hudson155.github.io/kairo/",
          githubRepoUrl = "https://github.com/hudson155/kairo",
        ),
      ),
    ),
  )

  server.startAndWait(
    release = { server.stop() },
  )
}
