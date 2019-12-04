package com.piperframework.exception.exception.conflict

abstract class EntityAlreadyExists(entityName: String) : ConflictException("$entityName already exists.")
