package com.piperframework.types

import kotlin.js.Date

/**
 * In JS, LocalDateTimes use the [String] class instead of an actual LocalDateTime class.
 */
actual typealias LocalDateTime = Date
