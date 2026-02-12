package kairo.adminSample

import io.r2dbc.spi.ConnectionFactory
import kairo.admin.AdminConfigSource
import kairo.admin.AdminDashboardFeature
import kairo.adminSample.author.AuthorApi
import kairo.adminSample.author.AuthorFeature
import kairo.adminSample.libraryBook.LibraryBookApi
import kairo.adminSample.libraryBook.LibraryBookFeature
import kairo.application.kairo
import kairo.config.loadConfig
import kairo.dependencyInjection.DependencyInjectionFeature
import kairo.healthCheck.HealthCheckFeature
import kairo.rest.RestFeature
import kairo.serialization.KairoJson
import kairo.server.Server
import kairo.sql.SqlFeature
import org.koin.core.Koin
import org.koin.dsl.koinApplication

public fun main(): Unit = kairo {
  val json = KairoJson()
  val config: Config = loadConfig(json = json)
  val koinApplication = koinApplication()
  val koin: Koin = koinApplication.koin

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
      AdminDashboardFeature(
        configSources = listOf(
          AdminConfigSource(
            "common",
            Thread.currentThread().contextClassLoader
              .getResource("config/common.conf")!!
              .readText(),
          ),
          AdminConfigSource(
            "development",
            Thread.currentThread().contextClassLoader
              .getResource("config/development.conf")!!
              .readText(),
          ),
        ),
        endpointClasses = listOf(
          LibraryBookApi.Get::class,
          LibraryBookApi.ListAll::class,
          LibraryBookApi.Create::class,
          LibraryBookApi.Update::class,
          LibraryBookApi.Delete::class,
          AuthorApi.Get::class,
          AuthorApi.ListAll::class,
          AuthorApi.Create::class,
          AuthorApi.Update::class,
          AuthorApi.Delete::class,
        ),
        connectionFactory = {
          try {
            koin.get<ConnectionFactory>()
          } catch (_: Exception) {
            null
          }
        },
      ),
    ),
  )

  server.startAndWait(
    release = { server.stop() },
  )
}
