package io.limberapp.framework.store

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import io.limberapp.framework.entity.CompleteEntity
import io.limberapp.framework.entity.CreationEntity
import io.limberapp.framework.entity.UpdateEntity
import java.util.UUID

/**
 * A Store is an interface to persistent storage for a particular entity. A number of default
 * methods are provided as part of this interface, but many Stores have additional methods as well.
 */
interface Store<Creation : CreationEntity, Complete : CompleteEntity, Update : UpdateEntity> {

    fun create(entity: Creation, typeRef: TypeReference<Complete>): Complete

    fun get(id: UUID, typeRef: TypeReference<Complete>): Complete?

    fun update(id: UUID, entity: Update, typeRef: TypeReference<Complete>): Complete

    fun delete(id: UUID)
}

inline fun <Creation : CreationEntity, reified Complete : CompleteEntity, Update : UpdateEntity>
        Store<Creation, Complete, Update>.create(entity: Creation) =
    create(entity, jacksonTypeRef())

inline fun <Creation : CreationEntity, reified Complete : CompleteEntity, Update : UpdateEntity>
        Store<Creation, Complete, Update>.get(id: UUID) =
    get(id, jacksonTypeRef())

inline fun <Creation : CreationEntity, reified Complete : CompleteEntity, Update : UpdateEntity>
        Store<Creation, Complete, Update>.update(id: UUID, updater: Update) =
    update(id, updater, jacksonTypeRef())
