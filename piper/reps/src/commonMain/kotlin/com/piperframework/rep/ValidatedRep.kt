package com.piperframework.rep

import com.piperframework.validation.RepValidation

interface ValidatedRep {

    fun validate(): RepValidation
}
