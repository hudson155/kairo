package io.limberapp.common.types

import java.time.ZoneId

class TimeZone(val zoneId: ZoneId) {
  override fun toString() = zoneId.toString()
}
