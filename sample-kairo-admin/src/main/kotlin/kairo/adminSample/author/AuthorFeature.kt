package kairo.adminSample.author

import io.ktor.server.application.Application
import kairo.dependencyInjection.HasKoinModules
import kairo.feature.Feature
import kairo.rest.HasRouting
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.koin.core.Koin
import org.koin.core.module.Module
import org.koin.dsl.module

public class AuthorFeature(
  private val koin: Koin,
) : Feature(), HasRouting, HasKoinModules {
  override val name: String = "Author"

  override val koinModules: List<Module> =
    listOf(
      module {
        single { AuthorMapper() }
        single { AuthorStore(get<R2dbcDatabase>()) }
        single { AuthorService(get()) }
        single { AuthorHandler(get(), get()) }
      },
    )

  override fun Application.routing() {
    with(koin.get<AuthorHandler>()) { routing() }
  }

  public companion object
}
