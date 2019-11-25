package io.limberapp.framework.ktorAuth

import com.auth0.jwt.interfaces.Payload
import io.ktor.auth.Principal

class LimberAuthPrincipal(val payload: Payload) : Principal
