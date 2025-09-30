package kairo.image

import io.kotest.matchers.shouldBe
import kairo.util.resource
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class ConvertImageTest {
  @Test
  fun test(): Unit =
    runTest {
      val jpeg = resource("image/Air_pohang.jpg").readBytes() // From Wikimedia commons.
      val png = resource("image/Air_pohang.png").readBytes()
      convertImage(jpeg, "png").shouldBe(png)
    }
}
