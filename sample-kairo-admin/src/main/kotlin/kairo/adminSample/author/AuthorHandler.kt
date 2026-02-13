package kairo.adminSample.author

import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import kairo.rest.HasRouting
import kairo.rest.auth.public
import kairo.rest.route

public class AuthorHandler(
  private val service: AuthorService,
  private val mapper: AuthorMapper,
) : HasRouting {
  @Suppress("LongMethod")
  override fun Application.routing() {
    routing {
      route(AuthorApi.Get::class) {
        auth { public() }
        handle {
          val model = service.get(AuthorId(endpoint.authorId))
          mapper.toRep(model)
        }
      }

      route(AuthorApi.ListAll::class) {
        auth { public() }
        handle {
          service.listAll().map { mapper.toRep(it) }
        }
      }

      route(AuthorApi.Create::class) {
        auth { public() }
        handle {
          val model = service.create(mapper.toModelCreator(endpoint.body))
          mapper.toRep(model)
        }
      }

      route(AuthorApi.Update::class) {
        auth { public() }
        handle {
          val model = service.update(
            AuthorId(endpoint.authorId),
            mapper.toModelUpdate(endpoint.body),
          )
          mapper.toRep(model)
        }
      }

      route(AuthorApi.Delete::class) {
        auth { public() }
        handle {
          val model = service.delete(AuthorId(endpoint.authorId))
          mapper.toRep(model)
        }
      }
    }
  }
}
