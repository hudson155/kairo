package io.limberapp.common.rep

import io.limberapp.common.validation.RepValidation

interface ValidatedRep {
  fun validate(): RepValidation
}
