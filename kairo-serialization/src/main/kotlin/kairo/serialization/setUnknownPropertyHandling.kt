package kairo.serialization

import com.fasterxml.jackson.databind.DeserializationFeature

internal fun ObjectMapperFactoryBuilder.setUnknownPropertyHandling() {
  configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, !allowUnknownProperties)
  configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, !allowUnknownProperties)
  configure(DeserializationFeature.FAIL_ON_UNEXPECTED_VIEW_PROPERTIES, !allowUnknownProperties)
}
