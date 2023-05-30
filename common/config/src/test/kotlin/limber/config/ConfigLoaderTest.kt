package limber.config

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldStartWith
import org.junit.jupiter.api.Test

internal class ConfigLoaderTest {
  private val config = ConfigLoaderTestConfig(
    clock = ClockConfig(type = ClockConfig.Type.Real),
    ids = IdsConfig(generation = IdsConfig.Generation.Random),
    name = "testing",
    server = ServerConfig(ServerConfig.Lifecycle(5000, 12_000)),
  )

  @Test
  fun `valid config`() {
    ConfigLoader.load<ConfigLoaderTestConfig>("valid-test-config")
      .shouldBe(config)
  }

  @Test
  fun `valid config with extension`() {
    val expected = config.copy(
      name = "testing-overwritten",
      server = config.server.let { server -> server.copy(lifecycle = server.lifecycle.copy(startupDelayMs = 0)) },
    )
    ConfigLoader.load<ConfigLoaderTestConfig>("valid-test-config-with-extension")
      .shouldBe(expected)
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
    val e = shouldThrowAny {
      ConfigLoader.load<ConfigLoaderTestConfig>("config-with-extra-property")
    }
    val cause = e.cause.shouldNotBeNull()
    cause.message.shouldStartWith("Unrecognized field \"extraProperty\"")
  }

  @Test
  fun `missing config property`() {
    val e = shouldThrowAny {
      ConfigLoader.load<ConfigLoaderTestConfig>("config-with-missing-property")
    }
    val cause = e.cause.shouldNotBeNull()
    cause.message.shouldContain("missing (therefore NULL) value for creator parameter clock")
  }

  @Test
  fun `invalid config property (wrong type)`() {
    val e = shouldThrowAny {
      ConfigLoader.load<ConfigLoaderTestConfig>("config-with-invalid-property")
    }
    val cause = e.cause.shouldNotBeNull()
    cause.message.shouldStartWith("Cannot deserialize value of type")
  }
}
