package com.piperframework.types

import java.time.ZoneId

actual class TimeZone(val zoneId: ZoneId) {
  override fun toString() = zoneId.toString()
}
