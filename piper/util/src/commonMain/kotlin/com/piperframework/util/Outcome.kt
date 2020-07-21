package com.piperframework.util

sealed class Outcome<T> {
  class Success<T>(val value: T) : Outcome<T>()

  class Failure<T>(val exception: Exception) : Outcome<T>()

  fun <R> fold(
    onSuccess: (value: T) -> R,
    onFailure: (exception: Exception) -> R
  ): R {
    return when (this) {
      is Success<T> -> onSuccess(value)
      is Failure<T> -> onFailure(exception)
    }
  }
}
