package kairo.protectedString

import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kairo.serialization.ObjectMapperFactory
import kairo.serialization.ObjectMapperFormat

internal class ProtectedStringTest : FunSpec({
  test("value") {
    @OptIn(ProtectedString.Access::class)
    ProtectedString("1").value.shouldBe("1")
  }

  test("equals method") {
    ProtectedString("1").shouldBe(ProtectedString("1"))
    ProtectedString("1").shouldNotBe(ProtectedString("2"))
  }

  test("hashCode method") {
    ProtectedString("1").hashCode().shouldBe("1".hashCode())
    ProtectedString("1").hashCode().shouldNotBe("2".hashCode())
  }

  test("toString method") {
    ProtectedString("1").hashCode().shouldBe("1".hashCode())
    ProtectedString("1").hashCode().shouldNotBe("2".hashCode())
  }

  context("serialization") {
    val mapper = ObjectMapperFactory.builder(ObjectMapperFormat.Json).build()

    test("serialize") {
      mapper.writeValueAsString(ProtectedString("1")).shouldBe("\"1\"")
    }

    test("deserialize") {
      mapper.readValue<ProtectedString>("\"1\"").shouldBe(ProtectedString("1"))
    }
  }
})
