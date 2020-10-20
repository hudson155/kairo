package io.limberapp.common.exception.notFound

abstract class EntityNotFound(entityName: String) : NotFoundException("$entityName not found.")
