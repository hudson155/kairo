package io.limberapp.common.testing

import io.limberapp.common.util.uuid.DeterministicUuidGenerator
import io.mockk.MockKAnnotations
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import java.time.Clock
import java.time.Instant
import java.time.ZoneId

/**
 * LEGACY CODE NOTE: At this time, [fixedClock] and [deterministicUuidGenerator] are separate instances of their
 * respective classes than the instances actually used by the test application. This should be changed when testing is
 * refactored or improved.
 */
abstract class AbstractResourceTest {
  protected abstract val limberTest: LimberTest

  val fixedClock: Clock = Clock.fixed(Instant.parse("2007-12-03T10:15:30.00Z"), ZoneId.of("America/New_York"))

  val deterministicUuidGenerator = DeterministicUuidGenerator()

  @BeforeEach
  fun beforeInternal() {
    MockKAnnotations.init(this)
    limberTest.start()
    before()
  }

  open fun before() {}

  @AfterEach
  fun afterInternal() {
    limberTest.stop()
  }

  open fun after() {}
}
