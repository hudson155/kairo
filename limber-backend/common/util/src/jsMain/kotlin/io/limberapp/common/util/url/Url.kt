package io.limberapp.common.util.url

external fun encodeURIComponent(uriComponent: String): String

actual fun enc(value: Any): String = encodeURIComponent(value.toString()).replace("%20", "+")
