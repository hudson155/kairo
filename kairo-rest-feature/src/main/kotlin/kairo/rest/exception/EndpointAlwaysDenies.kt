package kairo.rest.exception

import kairo.exception.ForbiddenException

public class EndpointAlwaysDenies : ForbiddenException(
  message = "Endpoint always denies.",
)
