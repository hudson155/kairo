package kairo.util

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class SlugifyTest {
  @Test
  fun kebab(): Unit =
    runTest {
      slugify("", delimiter = "-").shouldBe("")
      slugify("Hello", delimiter = "-").shouldBe("hello")
      slugify("HELLO", delimiter = "-").shouldBe("hello")
      slugify("   Hello   ", delimiter = "-").shouldBe("hello")
      slugify(" Con  | dãnas^t", delimiter = "-").shouldBe("con-danast")
      slugify("café", delimiter = "-").shouldBe("cafe")
      slugify("über", delimiter = "-").shouldBe("uber")
      slugify("façade", delimiter = "-").shouldBe("facade")
      slugify("maña!na", delimiter = "-").shouldBe("manana")
      slugify(" co-op ", delimiter = "-").shouldBe("co-op")
      slugify("a-b/c.d_e|f+g=h@i#j:k", delimiter = "-").shouldBe("a-b-c-d-e-f-g-h-i-j-k")
      slugify("a-/._|+=@#:k", delimiter = "-").shouldBe("a-k")
      slugify("élève étudiante", delimiter = "-").shouldBe("eleve-etudiante")
      slugify("中文 test", delimiter = "-").shouldBe("test")
      slugify("   multiple   spaces   ", delimiter = "-").shouldBe("multiple-spaces")
    }

  @Test
  fun snake(): Unit =
    runTest {
      slugify("", delimiter = "_").shouldBe("")
      slugify("Hello", delimiter = "_").shouldBe("hello")
      slugify("HELLO", delimiter = "_").shouldBe("hello")
      slugify("   Hello   ", delimiter = "_").shouldBe("hello")
      slugify(" Con  | dãnas^t", delimiter = "_").shouldBe("con_danast")
      slugify("café", delimiter = "_").shouldBe("cafe")
      slugify("über", delimiter = "_").shouldBe("uber")
      slugify("façade", delimiter = "_").shouldBe("facade")
      slugify("maña!na", delimiter = "_").shouldBe("manana")
      slugify(" co-op ", delimiter = "_").shouldBe("co_op")
      slugify("a-b/c.d_e|f+g=h@i#j:k", delimiter = "_").shouldBe("a_b_c_d_e_f_g_h_i_j_k")
      slugify("a-/._|+=@#:k", delimiter = "_").shouldBe("a_k")
      slugify("élève étudiante", delimiter = "_").shouldBe("eleve_etudiante")
      slugify("中文 test", delimiter = "_").shouldBe("test")
      slugify("   multiple   spaces   ", delimiter = "_").shouldBe("multiple_spaces")
    }
}
