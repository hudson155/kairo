package io.limberapp.framework.validation.util

import io.limberapp.framework.validation.Validation
import io.limberapp.framework.validator.Validator

fun Validation<String>.emailAddress() = boolean(Validator.emailAddress(subject))
