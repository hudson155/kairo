package io.limberapp.framework.store

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import io.limberapp.framework.model.CompleteModel
import io.limberapp.framework.model.CreationModel
import io.limberapp.framework.model.UpdateModel
import java.util.UUID

/**
 * A Store is an interface to persistent storage for a particular entity. A number of default
 * methods are provided as part of this interface, but many Stores have additional methods as well.
 */
interface Store<Creation : CreationModel, Complete : CompleteModel, Update : UpdateModel> {

    fun create(model: Creation, typeRef: TypeReference<Complete>): Complete

    fun getById(id: UUID, typeRef: TypeReference<Complete>): Complete?

    fun update(id: UUID, model: Update, typeRef: TypeReference<Complete>): Complete?
}

inline fun <Creation : CreationModel, reified Complete : CompleteModel, Update : UpdateModel>
        Store<Creation, Complete, Update>.create(model: Creation) =
    create(model, jacksonTypeRef())

inline fun <Creation : CreationModel, reified Complete : CompleteModel, Update : UpdateModel>
        Store<Creation, Complete, Update>.getById(id: UUID) =
    getById(id, jacksonTypeRef())

inline fun <Creation : CreationModel, reified Complete : CompleteModel, Update : UpdateModel>
        Store<Creation, Complete, Update>.update(id: UUID, updater: Update) =
    update(id, updater, jacksonTypeRef())
