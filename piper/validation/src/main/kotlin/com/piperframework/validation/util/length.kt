package com.piperframework.validation.util

import com.piperframework.validation.Validation

private const val MIN_CHARS = 3
private const val SHORT_TEXT_MAX_CHARS = 20
private const val MEDIUM_TEXT_MAX_CHARS = 100
private const val LONG_TEXT_MAX_CHARS = 10_000

fun Validation<String>.shortText(allowEmpty: Boolean) =
    length(IntRange(start = if (allowEmpty) 0 else MIN_CHARS, endInclusive = SHORT_TEXT_MAX_CHARS))

fun Validation<String>.mediumText(allowEmpty: Boolean) =
    length(IntRange(start = if (allowEmpty) 0 else MIN_CHARS, endInclusive = MEDIUM_TEXT_MAX_CHARS))

fun Validation<String>.longText(allowEmpty: Boolean) =
    length(IntRange(start = if (allowEmpty) 0 else MIN_CHARS, endInclusive = LONG_TEXT_MAX_CHARS))

fun Validation<String>.length(range: IntRange) = boolean(subject.length in range)
