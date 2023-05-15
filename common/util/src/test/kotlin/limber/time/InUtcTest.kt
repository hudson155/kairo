package limber.time

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

internal class InUtcTest {
  private val edmonton: ZonedDateTime = run {
    val localDate = LocalDate.of(2007, 12, 3)
    val localTime = LocalTime.of(5, 15, 30, 789_000_000)
    return@run ZonedDateTime.of(localDate, localTime, ZoneId.of("America/Edmonton"))
  }

  private val utc: ZonedDateTime = run {
    val localDate = LocalDate.of(2007, 12, 3)
    val localTime = LocalTime.of(12, 15, 30, 789_000_000)
    return@run ZonedDateTime.of(localDate, localTime, ZoneOffset.UTC)
  }

  @Test
  fun `in same time zone`() {
    utc.inUtc().shouldBe(utc)
  }

  @Test
  fun `in another time zone`() {
    edmonton.inUtc().shouldBe(utc)
  }
}
