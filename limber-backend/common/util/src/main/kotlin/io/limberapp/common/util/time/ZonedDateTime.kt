package io.limberapp.common.util.time

import java.time.ZoneId
import java.time.ZonedDateTime

private val UTC_ZONE_ID = ZoneId.of("UTC")

fun ZonedDateTime.inUTC(): ZonedDateTime = withZoneSameInstant(UTC_ZONE_ID)
