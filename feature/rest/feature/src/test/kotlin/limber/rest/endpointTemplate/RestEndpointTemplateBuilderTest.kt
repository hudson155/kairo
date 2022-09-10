package limber.rest.endpointTemplate

import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.ktor.http.HttpMethod
import limber.rest.RestEndpoint
import org.junit.jupiter.api.Test
import java.util.UUID

internal class RestEndpointTemplateBuilderTest {
  internal object List : RestEndpoint() {
    override val method: HttpMethod = HttpMethod.Get
    override val path: String = "/celebrities"
  }

  internal data class Get(val celebrityGuid: UUID) : RestEndpoint() {
    override val method: HttpMethod = HttpMethod.Get
    override val path: String = "/celebrities/$celebrityGuid"
  }

  @Test
  fun `object instance`() {
    RestEndpointTemplate.from(List::class).should { template ->
      template.method.shouldBe(HttpMethod.Get)
      template.path.shouldBe("/celebrities")
    }
  }

  @Test
  fun `data class`() {
    RestEndpointTemplate.from(Get::class).should { template ->
      template.method.shouldBe(HttpMethod.Get)
      template.path.shouldBe("/celebrities/{celebrityGuid}")
    }
  }
}
