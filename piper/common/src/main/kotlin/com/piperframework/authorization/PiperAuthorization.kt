package com.piperframework.authorization

import io.ktor.auth.Principal

interface PiperAuthorization<P : Principal> {

    fun authorize(principal: P?): Boolean
}
