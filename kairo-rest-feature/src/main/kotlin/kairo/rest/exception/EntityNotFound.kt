package kairo.rest.exception

import kairo.exception.NotFoundException

public class EntityNotFound : NotFoundException(
  message = "Entity not found.",
)
