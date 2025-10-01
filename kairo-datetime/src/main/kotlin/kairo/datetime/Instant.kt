package kairo.datetime

import kotlin.time.Instant

public val Instant.Companion.epoch: Instant
  get() = fromEpochMilliseconds(0)
