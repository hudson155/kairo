package limber.testing.should

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.ktor.client.call.body
import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.HttpStatusCode

public suspend inline fun <reified T : Any> shouldBeServerError(body: T, block: () -> Any?) {
  val e = shouldThrow<ServerResponseException> { block() }
  e.response.status.shouldBe(HttpStatusCode.InternalServerError)
  e.response.body<T>().shouldBe(body)
}
