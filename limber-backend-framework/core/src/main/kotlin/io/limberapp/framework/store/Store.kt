package io.limberapp.framework.store

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import io.limberapp.framework.model.Model
import java.util.UUID

/**
 * A Store is an interface to persistent storage for a particular entity. A number of default
 * methods are provided as part of this interface, but many Stores have additional methods as well.
 */
interface Store<M : Model<M>> {

    fun create(model: M, typeRef: TypeReference<M>): M

    fun getById(id: UUID, typeRef: TypeReference<M>): M?
}

inline fun <reified M : Model<M>> Store<M>.create(model: M) = create(model, jacksonTypeRef())

inline fun <reified M : Model<M>> Store<M>.getById(id: UUID) = getById(id, jacksonTypeRef())
