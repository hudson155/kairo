package io.limberapp.exception.notFound

abstract class EntityNotFound(entityName: String) : NotFoundException("$entityName not found.")
