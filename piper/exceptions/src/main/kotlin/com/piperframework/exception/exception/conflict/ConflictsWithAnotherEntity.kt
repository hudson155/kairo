package com.piperframework.exception.exception.conflict

abstract class ConflictsWithAnotherEntity(entityName: String) :
    ConflictException("$entityName conflicts with another $entityName.")
