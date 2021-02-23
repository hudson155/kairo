package io.limberapp.exception

abstract class EntityNotFound(
    entityName: String,
) : NotFoundException(
    message = "$entityName not found.",
    userVisibleProperties = mapOf("entityName" to entityName),
)
