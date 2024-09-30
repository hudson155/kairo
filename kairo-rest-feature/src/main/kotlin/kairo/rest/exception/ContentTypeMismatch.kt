package kairo.rest.exception

import kairo.exception.UnsupportedMediaTypeException

internal class ContentTypeMismatch : UnsupportedMediaTypeException(
  message = "This endpoint does not support the provided Content-Type header.",
)
