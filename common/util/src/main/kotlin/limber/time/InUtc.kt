package limber.time

import java.time.ZoneId
import java.time.ZonedDateTime

private val utcZoneId: ZoneId = ZoneId.of("UTC")

public fun ZonedDateTime.inUtc(): ZonedDateTime =
  withZoneSameInstant(utcZoneId)
