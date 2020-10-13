package io.limberapp.config.authentication

/**
 * There can be multiple ways to authenticate with an application. This configuration class encapsulates all
 * authentication mechanisms.
 *
 * For example, one mechanism may represent internal authentication, and there may be one or more other mechanisms that
 * represent third party identity providers.
 *
 * For tests, one might use unsigned authentication.
 */
data class AuthenticationConfig(
    val mechanisms: List<AuthenticationMechanism>,
)
