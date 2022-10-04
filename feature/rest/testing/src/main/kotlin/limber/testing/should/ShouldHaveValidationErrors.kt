package limber.testing.should

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpStatusCode
import limber.rep.ValidationErrorsRep

public suspend inline fun shouldHaveValidationErrors(vararg errors: Pair<String, String>, block: () -> Any?) {
  val e = shouldThrow<ClientRequestException> { block() }
  e.response.status.shouldBe(HttpStatusCode.BadRequest)
  val expected = errors.map {
    ValidationErrorsRep.ValidationError(propertyPath = it.first, message = it.second)
  }
  e.response.body<ValidationErrorsRep>().errors.shouldBe(expected)
}
