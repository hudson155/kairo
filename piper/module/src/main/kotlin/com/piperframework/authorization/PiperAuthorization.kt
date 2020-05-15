package com.piperframework.authorization

import io.ktor.auth.Principal

/**
 * Encapsulates some arbitrary way of authorizing access to an endpoint.
 */
interface PiperAuthorization<P : Principal> {
  /**
   * If this method returns true, the principal is authorized to access the endpoint. Otherwise, authorization is
   * forbidden.
   */
  fun authorize(principal: P?): Boolean
}
