package com.piperframework.rep

import com.piperframework.types.LocalDateTime

/**
 * Used for cases where the rep exists in the backend.
 * e.g. HTTP response bodies.
 */
interface CompleteRep {
  val createdDate: LocalDateTime
}
