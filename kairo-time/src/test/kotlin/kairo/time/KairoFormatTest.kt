package kairo.time

import io.kotest.matchers.shouldBe
import java.time.Duration
import java.time.temporal.ChronoUnit
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class KairoFormatTest {
  @Test
  fun `less than 1 second`(): Unit = runTest {
    Duration.ZERO.kairoFormat()
      .shouldBe("0.000 seconds")
    Duration.ZERO.plus(1, ChronoUnit.MILLIS).kairoFormat()
      .shouldBe("0.001 seconds")
    Duration.ZERO.plus(999, ChronoUnit.MILLIS).kairoFormat()
      .shouldBe("0.999 seconds")
  }

  @Test
  fun `less than 1 minute`(): Unit = runTest {
    Duration.ZERO.plus(1, ChronoUnit.SECONDS).kairoFormat()
      .shouldBe("1.000 second")
    Duration.ZERO.plus(1, ChronoUnit.SECONDS).plus(1, ChronoUnit.MILLIS).kairoFormat()
      .shouldBe("1.001 seconds")
    Duration.ZERO.plus(1, ChronoUnit.SECONDS).plus(999, ChronoUnit.MILLIS).kairoFormat()
      .shouldBe("1.999 seconds")
    Duration.ZERO.plus(2, ChronoUnit.SECONDS).kairoFormat()
      .shouldBe("2.000 seconds")
    Duration.ZERO.plus(2, ChronoUnit.SECONDS).plus(1, ChronoUnit.MILLIS).kairoFormat()
      .shouldBe("2.001 seconds")
    Duration.ZERO.plus(59, ChronoUnit.SECONDS).plus(999, ChronoUnit.MILLIS).kairoFormat()
      .shouldBe("59.999 seconds")
  }

  @Test
  fun `less than 1 hour`(): Unit = runTest {
    Duration.ZERO.plus(1, ChronoUnit.MINUTES).kairoFormat()
      .shouldBe("1 minute")
    Duration.ZERO.plus(1, ChronoUnit.MINUTES).plus(1, ChronoUnit.MILLIS).kairoFormat()
      .shouldBe("1 minute 0 seconds")
    Duration.ZERO.plus(1, ChronoUnit.MINUTES).plus(1, ChronoUnit.SECONDS).kairoFormat()
      .shouldBe("1 minute 1 second")
    Duration.ZERO.plus(1, ChronoUnit.MINUTES).plus(2, ChronoUnit.SECONDS).kairoFormat()
      .shouldBe("1 minute 2 seconds")
    Duration.ZERO.plus(1, ChronoUnit.MINUTES).plus(59, ChronoUnit.SECONDS).kairoFormat()
      .shouldBe("1 minute 59 seconds")
    Duration.ZERO.plus(2, ChronoUnit.MINUTES).kairoFormat()
      .shouldBe("2 minutes")
    Duration.ZERO.plus(2, ChronoUnit.MINUTES).plus(1, ChronoUnit.MILLIS).kairoFormat()
      .shouldBe("2 minutes 0 seconds")
    Duration.ZERO.plus(59, ChronoUnit.MINUTES).plus(59, ChronoUnit.SECONDS).kairoFormat()
      .shouldBe("59 minutes 59 seconds")
  }

  @Test
  fun `less than 1 day`(): Unit = runTest {
    Duration.ZERO.plus(1, ChronoUnit.HOURS).kairoFormat()
      .shouldBe("1 hour")
    Duration.ZERO.plus(1, ChronoUnit.HOURS).plus(1, ChronoUnit.MILLIS).kairoFormat()
      .shouldBe("1 hour 0 minutes")
    Duration.ZERO.plus(1, ChronoUnit.HOURS).plus(1, ChronoUnit.SECONDS).kairoFormat()
      .shouldBe("1 hour 0 minutes")
    Duration.ZERO.plus(1, ChronoUnit.HOURS).plus(1, ChronoUnit.MINUTES).kairoFormat()
      .shouldBe("1 hour 1 minute")
    Duration.ZERO.plus(1, ChronoUnit.HOURS).plus(2, ChronoUnit.MINUTES).kairoFormat()
      .shouldBe("1 hour 2 minutes")
    Duration.ZERO.plus(1, ChronoUnit.HOURS).plus(59, ChronoUnit.MINUTES).kairoFormat()
      .shouldBe("1 hour 59 minutes")
    Duration.ZERO.plus(2, ChronoUnit.HOURS).kairoFormat()
      .shouldBe("2 hours")
    Duration.ZERO.plus(2, ChronoUnit.HOURS).plus(1, ChronoUnit.MILLIS).kairoFormat()
      .shouldBe("2 hours 0 minutes")
    Duration.ZERO.plus(2, ChronoUnit.HOURS).plus(1, ChronoUnit.SECONDS).kairoFormat()
      .shouldBe("2 hours 0 minutes")
    Duration.ZERO.plus(23, ChronoUnit.HOURS).plus(59, ChronoUnit.MINUTES).kairoFormat()
      .shouldBe("23 hours 59 minutes")
  }

  @Test
  fun `1 day or more`(): Unit = runTest {
    Duration.ZERO.plus(1, ChronoUnit.DAYS).kairoFormat()
      .shouldBe("1 day")
    Duration.ZERO.plus(1, ChronoUnit.DAYS).plus(1, ChronoUnit.MILLIS).kairoFormat()
      .shouldBe("1 day 0 hours")
    Duration.ZERO.plus(1, ChronoUnit.DAYS).plus(1, ChronoUnit.SECONDS).kairoFormat()
      .shouldBe("1 day 0 hours")
    Duration.ZERO.plus(1, ChronoUnit.DAYS).plus(1, ChronoUnit.MINUTES).kairoFormat()
      .shouldBe("1 day 0 hours")
    Duration.ZERO.plus(1, ChronoUnit.DAYS).plus(1, ChronoUnit.HOURS).kairoFormat()
      .shouldBe("1 day 1 hour")
    Duration.ZERO.plus(1, ChronoUnit.DAYS).plus(2, ChronoUnit.HOURS).kairoFormat()
      .shouldBe("1 day 2 hours")
    Duration.ZERO.plus(1, ChronoUnit.DAYS).plus(23, ChronoUnit.HOURS).kairoFormat()
      .shouldBe("1 day 23 hours")
    Duration.ZERO.plus(2, ChronoUnit.DAYS).kairoFormat()
      .shouldBe("2 days")
    Duration.ZERO.plus(2, ChronoUnit.DAYS).plus(1, ChronoUnit.MILLIS).kairoFormat()
      .shouldBe("2 days 0 hours")
    Duration.ZERO.plus(2, ChronoUnit.DAYS).plus(1, ChronoUnit.SECONDS).kairoFormat()
      .shouldBe("2 days 0 hours")
    Duration.ZERO.plus(2, ChronoUnit.DAYS).plus(1, ChronoUnit.MINUTES).kairoFormat()
      .shouldBe("2 days 0 hours")
    Duration.ZERO.plus(999, ChronoUnit.DAYS).plus(23, ChronoUnit.HOURS).kairoFormat()
      .shouldBe("999 days 23 hours")
  }
}
