package io.limberapp.framework.testing

import io.limberapp.framework.config.Config
import io.limberapp.framework.config.jwt.JwtConfig
import io.limberapp.framework.config.serving.ServingConfig
import io.limberapp.framework.config.serving.StaticFiles
import io.limberapp.framework.jackson.objectMapper.LimberObjectMapper
import io.limberapp.framework.util.uuidGenerator.DeterministicUuidGenerator
import io.mockk.MockKAnnotations
import org.junit.Before
import java.time.Clock
import java.time.Instant
import java.time.ZoneId

abstract class AbstractResourceTest {

    protected val config = object : Config {
        override val serving = ServingConfig(
            apiPathPrefix = "/",
            staticFiles = StaticFiles(false)
        )
        override val jwt = JwtConfig(requireSignature = false)
    }

    protected abstract val limberTest: LimberTest

    protected val objectMapper = LimberObjectMapper()

    protected val fixedClock: Clock =
        Clock.fixed(Instant.parse("2007-12-03T10:15:30.00Z"), ZoneId.of("America/New_York"))

    protected val deterministicUuidGenerator = DeterministicUuidGenerator()

    @Before
    open fun before() {
        MockKAnnotations.init(this)
        deterministicUuidGenerator.reset()
    }
}

