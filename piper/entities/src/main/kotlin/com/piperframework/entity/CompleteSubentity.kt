@file:Suppress("UnnecessaryAbstractClass")

package com.piperframework.entity

import java.time.LocalDateTime

interface CompleteSubentity {
    val created: LocalDateTime
}
