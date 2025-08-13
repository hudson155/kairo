package kairo.cyclicBarrier

import kotlinx.coroutines.CopyableThrowable
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
public class CyclicBarrierException internal constructor(
  cause: Throwable,
) : Exception(cause), CopyableThrowable<CyclicBarrierException> {
  override fun createCopy(): Nothing? =
    null
}
