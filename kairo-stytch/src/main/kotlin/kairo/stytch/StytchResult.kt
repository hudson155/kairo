package kairo.stytch

import com.stytch.java.common.StytchException
import com.stytch.java.common.StytchResult
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger: KLogger = KotlinLogging.logger {}

public fun <T> StytchResult<T>.get(): T {
  when (this) {
    is StytchResult.Success -> {
      return value
    }
    is StytchResult.Error -> {
      when (val exception = exception) {
        is StytchException.Critical -> {
          logger.error(exception.reason) { "Critical Stytch error (response=${exception.response})." }
        }
        is StytchException.Response -> {
          logger.warn(exception) { "Stytch response exception (reason=${exception.reason})." }
        }
      }
      throw exception
    }
  }
}
