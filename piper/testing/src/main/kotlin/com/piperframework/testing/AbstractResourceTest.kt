package com.piperframework.testing

import com.piperframework.config.Config
import com.piperframework.config.authentication.AuthenticationConfig
import com.piperframework.config.authentication.AuthenticationMechanism
import com.piperframework.config.hashing.HashingConfig
import com.piperframework.config.serving.ServingConfig
import com.piperframework.config.serving.StaticFiles
import com.piperframework.util.uuid.DeterministicUuidGenerator
import io.mockk.MockKAnnotations
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import java.time.Clock
import java.time.Instant
import java.time.ZoneId

abstract class AbstractResourceTest {
  protected val config = object : Config {
    override val authentication =
      AuthenticationConfig(listOf(AuthenticationMechanism.UnsignedJwt))

    override val hashing = HashingConfig(logRounds = 4)

    override val serving = ServingConfig(
      redirectHttpToHttps = false,
      apiPathPrefix = "/",
      staticFiles = StaticFiles(false)
    )
  }

  protected abstract val piperTest: PiperTest

  val fixedClock: Clock = Clock.fixed(Instant.parse("2007-12-03T10:15:30.00Z"), ZoneId.of("America/New_York"))

  val deterministicUuidGenerator = DeterministicUuidGenerator()

  @BeforeEach
  fun beforeInternal() {
    MockKAnnotations.init(this)
    deterministicUuidGenerator.reset()
    piperTest.start()
    before()
  }

  open fun before() {}

  @AfterEach
  fun afterInternal() {
    piperTest.stop()
  }

  open fun after() {}
}
