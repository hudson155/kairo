package limber.feature.rest.endpointTemplate

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.ktor.http.HttpMethod
import limber.feature.rest.QueryParam
import limber.feature.rest.RestEndpoint
import org.junit.jupiter.api.Test
import java.util.UUID

internal class RestEndpointTemplateBuilderTest {
  internal data class Get(val celebrityGuid: UUID, val eventGuid: UUID) : RestEndpoint() {
    override val method: HttpMethod = HttpMethod.Get
    override val path: String = "/celebrities/$celebrityGuid"
    override val qp: List<QueryParam> = listOf(::eventGuid)
  }

  internal object GetAll : RestEndpoint() {
    override val method: HttpMethod = HttpMethod.Get
    override val path: String = "/celebrities"
  }

  @Test
  fun `data class`() {
    RestEndpointTemplate.from(Get::class).should { template ->
      template.method.shouldBe(HttpMethod.Get)
      template.path.shouldBe("/celebrities/{celebrityGuid}")
      template.requiredQueryParams.shouldContainExactlyInAnyOrder("eventGuid")
    }
  }

  @Test
  fun `object instance`() {
    RestEndpointTemplate.from(GetAll::class).should { template ->
      template.method.shouldBe(HttpMethod.Get)
      template.path.shouldBe("/celebrities")
      template.requiredQueryParams.shouldBeEmpty()
    }
  }
}
