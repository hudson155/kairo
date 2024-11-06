package kairo.googleCommon

import com.google.api.core.ApiFuture
import com.google.api.core.ApiFutureCallback
import com.google.api.core.ApiFutures
import com.google.common.util.concurrent.MoreExecutors
import java.util.concurrent.ExecutionException
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * Allows Google's [ApiFuture] class to work seamlessly with Kotlin coroutines.
 *
 * Adapted from https://github.com/JetBrains/teamcity-google-agent/blob/master/google-cloud-server/src/main/kotlin/jetbrains/buildServer/clouds/google/connector/ApiFuture.kt
 */
public suspend fun <T> ApiFuture<T>.await(): T {
  try {
    if (isDone) {
      @Suppress("BlockingMethodInNonBlockingContext") // Safe, since we already checked it's done.
      return get() as T
    }
  } catch (e: ExecutionException) {
    throw e.cause ?: e // Unwrap original cause from ExecutionException.
  }

  return suspendCancellableCoroutine { cont ->
    val callback = ContinuationCallback(cont)
    ApiFutures.addCallback(this, callback, MoreExecutors.directExecutor())
    cont.invokeOnCancellation {
      cancel(false)
      callback.cont = null // Clear the reference to continuation from the future's callback.
    }
  }
}

private class ContinuationCallback<T>(
  @Volatile @JvmField var cont: Continuation<T>?,
) : ApiFutureCallback<T> {
  override fun onSuccess(result: T) {
    cont?.resume(result)
  }

  override fun onFailure(t: Throwable) {
    cont?.resumeWithException(t)
  }
}
