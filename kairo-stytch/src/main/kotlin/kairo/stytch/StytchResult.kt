package kairo.stytch

import com.stytch.java.common.StytchResult

public fun <T> StytchResult<T>.get(): T =
  when (this) {
    is StytchResult.Success -> value
    is StytchResult.Error -> throw exception
  }
