package io.limberapp.framework.store

import io.limberapp.framework.entity.CompleteEntity
import java.util.UUID

/**
 * A CrStore is a Store that provides persistent storage for a particular entity, as well as Create
 * and Read operations from CRUD.
 */
interface CrStore<Complete : CompleteEntity> : Store<Complete> {

    fun create(entity: Complete)

    fun get(id: UUID): Complete?
}
