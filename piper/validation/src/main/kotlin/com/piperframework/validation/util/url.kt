package com.piperframework.validation.util

import com.piperframework.validation.Validation
import com.piperframework.validator.Validator

fun Validation<String>.path() = boolean(Validator.path(subject))

fun Validation<String>.url() = boolean(Validator.url(subject))
