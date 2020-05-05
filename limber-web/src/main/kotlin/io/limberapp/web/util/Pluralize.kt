package io.limberapp.web.util

import com.piperframework.util.unknownValue
import kotlin.math.abs

internal fun pluralize(string: String, count: Int) = when (string) {
    "members" -> pluralize("member", "members", count)
    "permissions" -> pluralize("permission", "permissions", count)
    else -> unknownValue("pluralization target", string)
}

private fun pluralize(singular: String, plural: String, count: Int): String {
    if (abs(count) == 1) return singular
    return plural
}
