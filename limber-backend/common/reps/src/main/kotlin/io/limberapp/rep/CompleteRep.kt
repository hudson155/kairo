package io.limberapp.rep

import java.time.LocalDateTime

/**
 * Used for cases where the rep exists in the backend.
 * e.g. HTTP response bodies.
 */
interface CompleteRep {
  val createdDate: LocalDateTime
}
