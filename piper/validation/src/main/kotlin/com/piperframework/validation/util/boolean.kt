package com.piperframework.validation.util

import com.piperframework.exception.exception.badRequest.ValidationException
import com.piperframework.validation.Validation

fun Validation<String>.boolean(boolean: Boolean) {
    if (!boolean) throw ValidationException(name)
}
