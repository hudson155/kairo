package kairo.util

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class CanonicalizeTest {
  @Test
  fun test(): Unit =
    runTest {
      canonicalize("").shouldBe("")
      canonicalize("Hello").shouldBe("hello")
      canonicalize("HELLO").shouldBe("hello")
      canonicalize("   Hello   ").shouldBe("hello")
      canonicalize(" Con  | dãnas^t").shouldBe("con danast")
      canonicalize("café").shouldBe("cafe")
      canonicalize("über").shouldBe("uber")
      canonicalize("façade").shouldBe("facade")
      canonicalize("maña!na").shouldBe("manana")
      canonicalize(" co-op ").shouldBe("co op")
      canonicalize("a-b/c.d_e|f+g=h@i#j:k").shouldBe("a b c d e f g h i j k")
      canonicalize("élève étudiante").shouldBe("eleve etudiante")
      canonicalize("中文 test").shouldBe("test")
      canonicalize("   multiple   spaces   ").shouldBe("multiple spaces")
    }
}
