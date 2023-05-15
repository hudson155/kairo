package limber.time

import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

private val utcZoneId: ZoneId = ZoneOffset.UTC

public fun ZonedDateTime.inUtc(): ZonedDateTime = withZoneSameInstant(utcZoneId)
