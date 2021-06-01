package io.limberapp.validation

import io.limberapp.util.darb.DarbEncoder

@Suppress("MagicNumber")
object Validator {
  fun auth0OrgId(value: String): Boolean =
      Regex.auth0OrgId.matches(value)

  fun base64EncodedUuid(value: String): Boolean =
      Regex.base64EncodedUuid.matches(value)

  fun darb(value: String): Boolean =
      DarbEncoder.getComponentsOrNull(value) != null

  fun emailAddress(value: String): Boolean =
      Regex.emailAddress.matches(value)

  fun featureName(value: String): Boolean =
      value.length in 3..20

  fun hostname(value: String): Boolean =
      Regex.hostname.matches(value)

  fun humanName(value: String): Boolean =
      value.length in 1..60

  fun length1hundred(value: String, allowEmpty: Boolean): Boolean =
      value.length in (if (allowEmpty) 0 else 1)..100

  fun length10thousand(value: String, allowEmpty: Boolean): Boolean =
      value.length in (if (allowEmpty) 0 else 1)..10_000

  fun orgName(value: String): Boolean =
      value.length in 3..40

  fun orgRoleName(value: String): Boolean =
      value.length in 3..40

  fun path(value: String): Boolean =
      Regex.path.matches(value)

  /**
   * This URL validator is definitely not perfect, but there really is no such thing as a perfect
   * URL validator - just one that fits the use case.
   */
  fun url(value: String): Boolean =
      Regex.url.matches(value)

  fun uuid(value: String): Boolean =
      Regex.uuid.matches(value)
}
