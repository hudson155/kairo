package io.limberapp.framework.authorization

import com.auth0.jwt.interfaces.Payload

interface LimberAuthorization {

    fun authorize(payload: Payload?): Boolean
}
