package com.piperframework.validation

import com.piperframework.rep.ValidatedRep
import kotlin.jvm.JvmName
import kotlin.reflect.KProperty1

/**
 * Validation class for [ValidatedRep]s.
 */
class RepValidation(validation: Builder.() -> Unit) {
  class Builder internal constructor() {
    val validations = mutableListOf<Pair<ValueValidation<*>, Boolean>>()

    fun validate(validate: RepValidation) {
      validations.addAll(validate.validations)
    }

    fun <R : ValidatedRep, T : Any?> R.validate(
      property: KProperty1<R, T>,
      validator: ValueValidation<T>.() -> Boolean,
    ) {
      val validation = ValueValidation(property.name, property.get(this))
      validations.add(validation to validation.validator())
    }

    /**
     * Validates a sub-rep.
     */
    @JvmName("validateRep")
    fun <R : ValidatedRep, T : ValidatedRep> R.validate(
      property: KProperty1<R, T>,
    ) {
      validate(property.get(this).validate())
    }

    /**
     * Validates a list of sub-reps.
     */
    @JvmName("validateReps")
    fun <R : ValidatedRep, T : List<ValidatedRep>> R.validate(
      property: KProperty1<R, T>,
    ) {
      property.get(this).forEach { validate(it.validate()) }
    }
  }

  private val validations = Builder().apply { validation() }.validations

  val isValid = validations.all { it.second }

  val firstInvalidPropertyName get() = validations.first { !it.second }.first.name
}
