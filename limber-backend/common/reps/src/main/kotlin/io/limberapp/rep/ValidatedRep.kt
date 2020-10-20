package io.limberapp.rep

import io.limberapp.common.validation.RepValidation

interface ValidatedRep {
  fun validate(): RepValidation
}
