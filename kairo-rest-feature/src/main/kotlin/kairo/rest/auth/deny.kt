package kairo.rest.auth

import kairo.rest.exception.EndpointAlwaysDenies

/**
 * Denies all requests.
 */
@Suppress("UnusedReceiverParameter")
public fun Auth.deny(): Auth.Result =
  Auth.Result.Exception(EndpointAlwaysDenies())
