package kairo.googleCloudSchedulerRest

import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import kairo.googleCloudTasks.GoogleCloudTasks
import kairo.googleCloudTasksTesting.getCreatedTasks
import kairo.rest.endpoint.RestEndpointDetails
import kairo.restTesting.client
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class GoogleCloudSchedulerCreateTaskTest : GoogleCloudSchedulerRestFeatureTest() {
  @Test
  fun `happy path`(): Unit = runTest {
    val task = GoogleCloudTasks.Task(
      queueName = "library",
      details = RestEndpointDetails(
        method = HttpMethod.Post,
        path = "/library-books/:libraryBookId/read",
        contentType = ContentType.Application.Json,
        accept = ContentType.Application.Json,
        body = null,
      ),
    )
    client.request(GoogleCloudSchedulerApi.CreateTask(task))
      .shouldBe(Unit)

    getCreatedTasks(injector).shouldContainExactlyInAnyOrder(task)
  }
}
