package com.piperframework.ktorAuth

import com.auth0.jwt.interfaces.Payload
import io.ktor.auth.Principal

class PiperAuthPrincipal(val payload: Payload) : Principal
