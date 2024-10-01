package kairo.rest.auth

/**
 * Allows all requests, including unauthenticated requests without an auth principal at all.
 */
@Suppress("UnusedReceiverParameter")
public fun Auth.public(): Auth.Result =
  Auth.Result.Success
