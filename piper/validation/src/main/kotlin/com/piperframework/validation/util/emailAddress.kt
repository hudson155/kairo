package com.piperframework.validation.util

import com.piperframework.validation.Validation
import com.piperframework.validator.Validator

fun Validation<String>.emailAddress() = boolean(Validator.emailAddress(subject))
