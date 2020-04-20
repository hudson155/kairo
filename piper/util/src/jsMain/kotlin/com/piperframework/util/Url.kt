package com.piperframework.util

external fun encodeURIComponent(uriComponent: String): String

actual fun enc(value: Any): String = encodeURIComponent(value.toString())
