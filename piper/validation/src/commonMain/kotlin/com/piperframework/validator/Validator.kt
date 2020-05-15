package com.piperframework.validator

import com.piperframework.util.darb.DarbEncoder

/**
 * This object contains methods to validate primitive inputs.
 */
@Suppress("MagicNumber")
object Validator {
  fun auth0ClientId(value: String) = Regex.auth0ClientId.matches(value)

  fun base64EncodedUuid(value: String) = Regex.base64EncodedUuid.matches(value)

  fun darb(value: String) = DarbEncoder.getComponentsOrNull(value) != null

  fun emailAddress(value: String) = Regex.emailAddress.matches(value)

  fun featureName(value: String) = value.length in 3..20

  fun hostname(value: String) = Regex.hostname.matches(value)

  fun humanName(value: String) = value.length in 1..40

  fun length1hundred(value: String, allowEmpty: Boolean) = value.length in (if (allowEmpty) 0 else 1)..100

  fun length10thousand(value: String, allowEmpty: Boolean) = value.length in (if (allowEmpty) 0 else 1)..10_000

  fun orgName(value: String) = value.length in 3..40

  fun orgRoleName(value: String) = value.length in 3..40

  fun path(value: String) = Regex.path.matches(value)

  /**
   * This URL validator is definitely not perfect, but there really is no such thing as a perfect URL validator - just
   * one that fits the use case.
   */
  fun url(value: String) = Regex.url.matches(value)

  fun uuid(value: String) = Regex.uuid.matches(value)
}
