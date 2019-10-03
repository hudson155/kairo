package io.limberapp.framework.model

import java.util.UUID

/**
 * This is the generic class for all models. It includes default properties, and is configured to
 * work with the database layer and the API layer. All data classes representing models at either of
 * these two layers should inherit from this interface.
 *
 * Models have creator-unknown properties, which are properties that cannot be known until the
 * entity is persisted. By default, this includes only the id property, but could include additional
 * properties as well
 */
abstract class Model<Self : Model<Self>> : PartialModel {

    abstract val id: UUID?

    /**
     * Returns a new Model instance with id as the ID. Ensures that the ID is null before setting
     * it.
     */
    fun withId(id: UUID): Self {
        check(this.id == null)
        return setId(id)
    }

    /**
     * This method returns a new instance of the model with all creator-unknown properties as null.
     */
    open fun asCreator() = setId(null)

    protected abstract fun setId(id: UUID?): Self

    /**
     * This method returns a new instance of the model with all creator-unknown properties as
     * non-null.
     */
    open fun asResult(): Self {
        check(id != null)
        @Suppress("UNCHECKED_CAST", "UnsafeCast") // This is safe given how Self works.
        return this as Self
    }
}
