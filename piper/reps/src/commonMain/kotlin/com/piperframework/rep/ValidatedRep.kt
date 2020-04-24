package com.piperframework.rep

import com.piperframework.validation.RepValidation

/**
 * A rep that is validatable.
 */
interface ValidatedRep {
    fun validate(): RepValidation
}
