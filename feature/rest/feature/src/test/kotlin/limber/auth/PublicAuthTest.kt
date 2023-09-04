package limber.auth

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.Test

internal class PublicAuthTest {
  @Test
  fun test() {
    val context = context()
    shouldNotThrowAny {
      test(context, Auth.Public)
    }
  }

  private fun context(): RestContext =
    RestContext(authorize = true, claimPrefix = "", principal = mockk())

  private fun test(context: RestContext, auth: Auth.Public) {
    runBlocking {
      withContext(context) {
        auth { auth }
      }
    }
  }
}
