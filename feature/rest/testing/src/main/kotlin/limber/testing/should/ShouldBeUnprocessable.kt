package limber.testing.should

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpStatusCode
import limber.rep.UnprocessableErrorRep

public suspend inline fun shouldBeUnprocessable(message: String, block: () -> Unit) {
  val e = shouldThrow<ClientRequestException> { block() }
  e.response.status.shouldBe(HttpStatusCode.UnprocessableEntity)
  e.response.body<UnprocessableErrorRep>().message.shouldBe(message)
}
