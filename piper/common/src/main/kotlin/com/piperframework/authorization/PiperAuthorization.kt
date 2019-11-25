package com.piperframework.authorization

import com.auth0.jwt.interfaces.Payload

interface PiperAuthorization {

    fun authorize(payload: Payload?): Boolean
}
