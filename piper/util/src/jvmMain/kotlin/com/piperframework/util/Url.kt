@file:JvmName("UrlJvmKt")

package com.piperframework.util

import java.net.URLEncoder

actual fun enc(value: Any): String = URLEncoder.encode(value.toString(), "UTF-8")
