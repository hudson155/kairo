package io.limberapp.common.rep

import java.time.ZonedDateTime

/**
 * Used for cases where the rep exists in the backend.
 * e.g. HTTP response bodies.
 */
interface CompleteRep {
  val createdDate: ZonedDateTime
}
