package kairo.datetime

import io.kotest.matchers.longs.shouldBeZero
import kotlin.time.Instant
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class EpochTest {
  @Test
  fun test(): Unit =
    runTest {
      Instant.epoch.epochSeconds.shouldBeZero()
    }
}
