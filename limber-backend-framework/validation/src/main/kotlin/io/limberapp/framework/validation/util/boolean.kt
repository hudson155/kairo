package io.limberapp.framework.validation.util

import io.limberapp.framework.validation.Validation
import io.limberapp.framework.validation.ValidationException

fun Validation<String>.boolean(boolean: Boolean) {
    if (!boolean) throw ValidationException(name)
}
