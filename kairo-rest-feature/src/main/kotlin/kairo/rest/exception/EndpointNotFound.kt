package kairo.rest.exception

import kairo.exception.NotFoundException

internal class EndpointNotFound : NotFoundException(
  message = "This endpoint does not exist." +
    " Check the method, path, and query params.",
)
