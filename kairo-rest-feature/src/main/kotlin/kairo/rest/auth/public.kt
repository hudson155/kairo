package kairo.rest.auth

/**
 * Allows all requests, including unauthenticated requests without an auth principal at all.
 */
public fun Auth.public(): Auth.Success =
  Auth.Success
