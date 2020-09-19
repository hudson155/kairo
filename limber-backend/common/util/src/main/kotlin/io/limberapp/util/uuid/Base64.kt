package io.limberapp.util.uuid

import java.util.*

fun UUID.base64Encode(): String = Base64.getEncoder().encodeToString(asByteArray())

fun uuidFromBase64Encoded(base64Encoded: String): UUID = uuidFromByteArray(Base64.getDecoder().decode(base64Encoded))
