package kairo.hashing

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class HashingTest {
  @Test
  fun md5(): Unit = runTest {
    "".md5().shouldBe("d41d8cd98f00b204e9800998ecf8427e")
    "Hello, Baeldung!".md5().shouldBe("6469a4ea9e2753755f5120beb51587f8")
  }
}
