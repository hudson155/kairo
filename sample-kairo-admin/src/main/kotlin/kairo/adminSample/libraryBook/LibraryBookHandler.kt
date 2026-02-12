package kairo.adminSample.libraryBook

import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import kairo.rest.HasRouting
import kairo.rest.auth.public
import kairo.rest.route

public class LibraryBookHandler(
  private val service: LibraryBookService,
  private val mapper: LibraryBookMapper,
) : HasRouting {
  @Suppress("LongMethod")
  override fun Application.routing() {
    routing {
      route(LibraryBookApi.Get::class) {
        auth { public() }
        handle {
          val model = service.get(LibraryBookId(endpoint.libraryBookId))
          mapper.toRep(model)
        }
      }

      route(LibraryBookApi.ListAll::class) {
        auth { public() }
        handle {
          service.listAll().map { mapper.toRep(it) }
        }
      }

      route(LibraryBookApi.Create::class) {
        auth { public() }
        handle {
          val model = service.create(mapper.toModelCreator(endpoint.body))
          mapper.toRep(model)
        }
      }

      route(LibraryBookApi.Update::class) {
        auth { public() }
        handle {
          val model = service.update(
            LibraryBookId(endpoint.libraryBookId),
            mapper.toModelUpdate(endpoint.body),
          )
          mapper.toRep(model)
        }
      }

      route(LibraryBookApi.Delete::class) {
        auth { public() }
        handle {
          val model = service.delete(LibraryBookId(endpoint.libraryBookId))
          mapper.toRep(model)
        }
      }
    }
  }
}
