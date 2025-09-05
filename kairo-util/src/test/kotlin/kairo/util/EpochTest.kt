package kairo.util

import io.kotest.matchers.longs.shouldBeZero
import kotlin.time.Instant
import org.junit.jupiter.api.Test

internal class EpochTest {
  @Test
  fun test() {
    Instant.epoch.epochSeconds.shouldBeZero()
  }
}
