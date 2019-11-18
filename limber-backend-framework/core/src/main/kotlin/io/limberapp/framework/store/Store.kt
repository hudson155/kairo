package io.limberapp.framework.store

import io.limberapp.framework.entity.CompleteEntity
import io.limberapp.framework.entity.UpdateEntity
import java.util.UUID

/**
 * A Store is an interface to persistent storage for a particular entity. A number of default
 * methods are provided as part of this interface, but many Stores have additional methods as well.
 */
interface Store<Complete : CompleteEntity, Update : UpdateEntity> {

    fun create(entity: Complete)

    fun get(id: UUID): Complete?

    fun update(id: UUID, update: Update): Complete

    fun delete(id: UUID)
}
