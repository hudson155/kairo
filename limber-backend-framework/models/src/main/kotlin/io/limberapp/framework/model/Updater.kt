package io.limberapp.framework.model

/**
 * Updaters are classes that update models. They are used for PATCH request bodies.
 */
interface Updater<M : Model<M>>
