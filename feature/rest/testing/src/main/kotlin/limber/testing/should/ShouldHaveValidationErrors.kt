package limber.testing.should

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpStatusCode.Companion.UnprocessableEntity
import limber.rep.ValidationErrorsRep

public suspend fun shouldHaveValidationErrors(
  vararg errors: Pair<String, String>,
  block: suspend () -> Any?,
) {
  val e = shouldThrow<ClientRequestException> { block() }
  e.response.status.shouldBe(UnprocessableEntity)
  val expected = errors.map {
    ValidationErrorsRep.ValidationError(propertyPath = it.first, message = it.second)
  }
  e.response.body<ValidationErrorsRep>().errors.shouldBe(expected)
}
