package limber.testing.should

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpStatusCode
import limber.rep.ConflictErrorRep

public suspend inline fun shouldBeConflict(message: String, block: () -> Any?) {
  val e = shouldThrow<ClientRequestException> { block() }
  e.response.status.shouldBe(HttpStatusCode.Conflict)
  e.response.body<ConflictErrorRep>().message.shouldBe(message)
}
