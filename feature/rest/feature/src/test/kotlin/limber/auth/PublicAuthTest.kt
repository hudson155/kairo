package limber.auth

import io.kotest.matchers.booleans.shouldBeTrue
import io.mockk.mockk
import org.junit.jupiter.api.Test

internal class PublicAuthTest {
  @Test
  fun test() {
    val context = context()
    Auth.Public.authorize(context)
      .shouldBeTrue()
  }

  private fun context(): RestContext = mockk()
}
