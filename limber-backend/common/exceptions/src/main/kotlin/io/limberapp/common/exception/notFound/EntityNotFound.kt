package io.limberapp.common.exception.notFound

abstract class EntityNotFound(
    entityName: String,
) : NotFoundException(
    message = "$entityName not found.",
    userVisibleProperties = mapOf("entityName" to entityName),
)
