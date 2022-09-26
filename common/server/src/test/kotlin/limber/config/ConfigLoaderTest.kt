package limber.config

import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldStartWith
import org.junit.jupiter.api.Test

internal class ConfigLoaderTest {
  private val config = ConfigLoaderTestConfig(
    clock = ClockConfig(type = ClockConfig.Type.Real),
    guids = GuidsConfig(generation = GuidsConfig.Generation.Random),
    name = "testing",
    server = ServerConfig(ServerConfig.Lifecycle(5000, 12_000)),
  )

  @Test
  fun `valid config`() {
    ConfigLoader.load<ConfigLoaderTestConfig>("valid-test-config")
      .shouldBe(config)
  }

  @Test
  fun `missing config`() {
    val e = shouldThrow<IllegalArgumentException> {
      ConfigLoader.load<ConfigLoaderTestConfig>("missing-config")
    }
    e.message.shouldBe("resource config/missing-config.yaml not found.")
  }

  @Test
  fun `extra config property`() {
    val e = shouldThrow<UnrecognizedPropertyException> {
      ConfigLoader.load<ConfigLoaderTestConfig>("config-with-extra-property")
    }
    e.message.shouldStartWith("Unrecognized field \"extraProperty\"")
  }

  @Test
  fun `missing config property`() {
    val e = shouldThrow<MissingKotlinParameterException> {
      ConfigLoader.load<ConfigLoaderTestConfig>("config-with-missing-property")
    }
    e.message.shouldContain("missing (therefore NULL) value for creator parameter clock")
  }

  @Test
  fun `invalid config property (wrong type)`() {
    val e = shouldThrow<MismatchedInputException> {
      ConfigLoader.load<ConfigLoaderTestConfig>("config-with-invalid-property")
    }
    e.message.shouldStartWith("Cannot deserialize value of type")
  }
}
