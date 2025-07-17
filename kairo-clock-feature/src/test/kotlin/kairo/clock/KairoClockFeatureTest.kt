package kairo.clock

import com.google.inject.Guice
import com.google.inject.Stage
import io.kotest.assertions.withClue
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.should
import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import kairo.dependencyInjection.getInstance
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class KairoClockFeatureTest {
  private val tolerance: Duration = Duration.ofMillis(100)

  @Test
  fun `system, utc`(): Unit = runTest {
    val config = KairoClockConfig.System(ZoneOffset.UTC)
    val clock = createClock(config)
    Instant.now(clock).shouldBeCloseToNow(tolerance)
  }

  @Test
  fun `system, non-utc`(): Unit = runTest {
    val config = KairoClockConfig.System(ZoneId.of("America/Edmonton"))
    val clock = createClock(config)
    Instant.now(clock).shouldBeCloseToNow(tolerance)
  }

  @Test
  fun `fixed, utc`(): Unit = runTest {
    val config = KairoClockConfig.Fixed(Instant.parse("2023-11-13T19:44:32.123456789Z"), ZoneOffset.UTC)
    val clock = createClock(config)
    Instant.now(clock).shouldBeCloseTo(Instant.parse("2023-11-13T19:44:32.123456789Z"), tolerance)
  }

  @Test
  fun `fixed, non-utc`(): Unit = runTest {
    val config = KairoClockConfig.Fixed(Instant.parse("2023-11-13T19:44:32.123456789Z"), ZoneId.of("America/Edmonton"))
    val clock = createClock(config)
    Instant.now(clock).shouldBeCloseTo(Instant.parse("2023-11-13T19:44:32.123456789Z"), tolerance)
  }

  private fun createClock(config: KairoClockConfig): Clock {
    val injector = Guice.createInjector(Stage.PRODUCTION, KairoClockFeature(config))
    return injector.getInstance<Clock>()
  }

  /**
   * Inspired by [io.kotest.matchers.date.shouldBeCloseTo].
   */
  @Suppress("ForbiddenMethodCall")
  private fun Instant.shouldBeCloseToNow(tolerance: Duration) {
    shouldBeCloseTo(Instant.now(), tolerance)
  }

  /**
   * Inspired by [io.kotest.matchers.date.shouldBeCloseTo].
   */
  private fun Instant.shouldBeCloseTo(other: Instant, tolerance: Duration) {
    should { instant ->
      val diff = Duration.between(instant, other).abs()
      val closeDuration = diff.minus(tolerance)
      withClue({ "Expected $this to be close to $other in range by $tolerance but it's not." }) {
        closeDuration.isPositive.shouldBeFalse()
      }
    }
  }
}
