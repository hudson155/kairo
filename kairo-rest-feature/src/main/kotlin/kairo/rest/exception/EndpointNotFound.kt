package kairo.rest.exception

import kairo.exception.NotFoundException

public class EndpointNotFound : NotFoundException(
  message = "This endpoint does not exist." +
    " Check the method, path, accept header, content type header, and query params.",
)
