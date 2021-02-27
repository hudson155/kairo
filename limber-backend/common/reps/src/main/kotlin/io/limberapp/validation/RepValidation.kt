package io.limberapp.validation

import io.limberapp.rep.ValidatedRep
import kotlin.reflect.KProperty1

/**
 * Validation class for [ValidatedRep]s.
 */
class RepValidation(validation: Builder.() -> Unit) {
  class Builder internal constructor() {
    private val validations: MutableList<ValueValidation<*>> = mutableListOf()

    fun <R : ValidatedRep, T : Any?> R.validate(
        property: KProperty1<R, T>,
        validator: T.() -> Boolean,
    ) {
      val value = property.get(this)
      val validation = ValueValidation(property.name, value, value.validator())
      validations.add(validation)
    }

    @JvmName("validateRep")
    fun <R : ValidatedRep, T : ValidatedRep> R.validate(property: KProperty1<R, T>) {
      val subValidations = property.get(this).validate().validations
      validations.addAll(subValidations.map { it.withNamePrefix("${property.name}.") })
    }

    @JvmName("validateReps")
    fun <R : ValidatedRep, T : Collection<ValidatedRep>> R.validate(property: KProperty1<R, T>) {
      property.get(this).forEachIndexed { i, rep ->
        val subValidations = rep.validate().validations
        validations.addAll(subValidations.map { it.withNamePrefix("${property.name}[$i].") })
      }
    }

    internal fun build(): List<ValueValidation<*>> = validations
  }

  private val validations: List<ValueValidation<*>> =
      Builder().apply { validation() }.build()

  val isValid: Boolean = validations.all { it.isValid }

  val invalidPropertyNames: List<String> by lazy {
    validations.filter { !it.isValid }.map { it.name }
        .also { check(it.isNotEmpty()) }
  }
}
