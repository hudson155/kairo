package kairo.adminSample.libraryBook

import io.ktor.server.application.Application
import kairo.dependencyInjection.HasKoinModules
import kairo.feature.Feature
import kairo.rest.HasRouting
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.koin.core.Koin
import org.koin.core.module.Module
import org.koin.dsl.module

public class LibraryBookFeature(
  private val koin: Koin,
) : Feature(), HasRouting, HasKoinModules {
  override val name: String = "Library Book"

  override val koinModules: List<Module> =
    listOf(
      module {
        single { LibraryBookMapper() }
        single { LibraryBookStore(get<R2dbcDatabase>()) }
        single { LibraryBookService(get()) }
        single { LibraryBookHandler(get(), get()) }
      },
    )

  override fun Application.routing() {
    with(koin.get<LibraryBookHandler>()) { routing() }
  }

  public companion object
}
