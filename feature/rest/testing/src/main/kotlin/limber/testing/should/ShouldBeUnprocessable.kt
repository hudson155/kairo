package limber.testing.should

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpStatusCode

public suspend fun shouldBeUnprocessable(block: suspend () -> Any?) {
  val e = shouldThrow<ClientRequestException> { block() }
  e.response.status.shouldBe(HttpStatusCode.UnprocessableEntity)
}
