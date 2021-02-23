package io.limberapp.rep

import io.limberapp.validation.RepValidation

interface ValidatedRep {
  fun validate(): RepValidation
}
