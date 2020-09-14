package io.limberapp.common.rep

import io.limberapp.common.validation.RepValidation

/**
 * A rep that is validatable.
 */
interface ValidatedRep {
  fun validate(): RepValidation
}
