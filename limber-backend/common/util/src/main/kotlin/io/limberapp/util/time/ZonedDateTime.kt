package io.limberapp.util.time

import java.time.ZoneOffset
import java.time.ZonedDateTime

/**
 * Returns another [ZonedDateTime] at the same instant in time, but in UTC. For example, 9:00pm MST
 * would become 2:00pm UTC.
 */
fun ZonedDateTime.inUTC(): ZonedDateTime = withZoneSameInstant(ZoneOffset.UTC)
