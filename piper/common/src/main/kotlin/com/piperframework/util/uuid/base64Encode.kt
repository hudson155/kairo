package com.piperframework.util.uuid

import java.util.Base64
import java.util.UUID

fun UUID.base64Encode(): String = Base64.getEncoder().encodeToString(asByteArray())
