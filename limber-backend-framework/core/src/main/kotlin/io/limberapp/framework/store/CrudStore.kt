package io.limberapp.framework.store

import io.limberapp.framework.entity.CompleteEntity
import io.limberapp.framework.entity.UpdateEntity
import java.util.UUID

/**
 * A CrudStore is a Store that provides persistent storage for a particular entity, as well as
 * Create, Read, Update, and Delete operations from CRUD.
 */
interface CrudStore<Complete : CompleteEntity, Update : UpdateEntity> : Store<Complete> {

    fun create(entity: Complete)

    fun get(id: UUID): Complete?

    fun update(id: UUID, update: Update): Complete?

    fun delete(id: UUID): Unit?
}
