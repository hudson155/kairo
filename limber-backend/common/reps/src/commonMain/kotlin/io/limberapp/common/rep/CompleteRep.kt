package io.limberapp.common.rep

import io.limberapp.common.types.LocalDateTime

/**
 * Used for cases where the rep exists in the backend.
 * e.g. HTTP response bodies.
 */
interface CompleteRep {
  val createdDate: LocalDateTime
}
