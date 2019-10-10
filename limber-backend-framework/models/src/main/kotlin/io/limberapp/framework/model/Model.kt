package io.limberapp.framework.model

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.LocalDateTime
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
abstract class Model<Self : Model<Self>> : PartialModel() {

    abstract val id: UUID?
    abstract val created: LocalDateTime?
    abstract val version: Int?

    val modelState
        @JsonIgnore get() = when {
            id == null && created == null && version == null -> ModelState.CREATION
            id != null && created != null && version != null -> ModelState.COMPLETE
            else -> error("This model is not in a valid state")
        }

    enum class ModelState { CREATION, COMPLETE }

    /**
     * Returns a new Model instance with id as the ID. Ensures that the ID is null before setting
     * it.
     */
    fun complete(id: UUID, created: LocalDateTime): Self {
        check(modelState == ModelState.CREATION)
        return setFields(id, created, 0)
    }

    /**
     * This method returns a new instance of the model with all creator-unknown properties as null.
     */
    fun asCreator() = setFields(null, null, null)

    /**
     * This method returns a new instance of the model with all creator-unknown properties as
     * non-null.
     */
    fun asResult(): Self {
        check(modelState == ModelState.COMPLETE)
        @Suppress("UNCHECKED_CAST", "UnsafeCast") // This is safe given how Self works.
        return this as Self
    }

    protected abstract fun setFields(id: UUID?, created: LocalDateTime?, version: Int?): Self
}
