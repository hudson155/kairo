package io.limberapp.common.types

import java.time.ZoneId

actual class TimeZone(val zoneId: ZoneId) {
  override fun toString() = zoneId.toString()
}
