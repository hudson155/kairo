package com.piperframework.util

sealed class Outcome<T> {
  abstract val value: T

  class Success<T>(override val value: T) : Outcome<T>()

  class Failure<T>(val exception: Exception) : Outcome<T>() {
    override val value get() = error(exception.toString())
  }

  /**
   * If [Success], calls [action].
   * If [Failure], no-op.
   */
  inline fun onSuccess(action: (value: T) -> Unit) {
    if (this is Success<T>) action(value)
  }

  /**
   * If [Success], no-op.
   * If [Failure], calls [action].
   */
  inline fun onFailure(action: (exception: Exception) -> Unit) {
    if (this is Failure<T>) action(exception)
  }

  /**
   * If [Success], calls [transform] and returns a [Success] with the result.
   * If [Failure], identity.
   */
  inline fun <R> map(transform: (T) -> Outcome<R>): Outcome<R> = when (this) {
    is Success<T> -> transform(value)
    is Failure<T> -> Failure(exception)
  }

  /**
   * If [Success], calls [successAction].
   * If [Failure], calls [failureAction].
   */
  inline fun fold(
    successAction: (value: T) -> Unit,
    failureAction: (exception: Exception) -> Unit,
  ) {
    when (this) {
      is Success<T> -> successAction(value)
      is Failure<T> -> failureAction(exception)
    }
  }
}
