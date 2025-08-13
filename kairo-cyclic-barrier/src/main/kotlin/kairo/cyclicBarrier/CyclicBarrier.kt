package kairo.cyclicBarrier

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Kotlin coroutine equivalent of Java's [java.util.concurrent.CyclicBarrier].
 * See https://en.wikipedia.org/wiki/Barrier_(computer_science) to understand barriers.
 * Called "cyclic" because it can be re-used after being broken.
 */
public class CyclicBarrier(
  /**
   * The number of parties that must call [await] before the barrier breaks.
   */
  private val parties: Int,
  /**
   * This command will be executed after the barrier is broken,
   * before any of the parties are released.
   */
  private val barrierCommand: (suspend () -> Unit)? = null,
) {
  private val lock: Mutex = Mutex()
  private var waiting: Int = 0
  private var gate: CompletableDeferred<Unit> = CompletableDeferred()

  init {
    require(parties > 0) { "Parties must be positive." }
  }

  public suspend fun await() {
    val gate: CompletableDeferred<Unit>
    lock.withLock {
      this.waiting++
      gate = this.gate
      if (this.waiting == this.parties) {
        breakBarrier()
      }
    }
    gate.await()
  }

  private suspend fun breakBarrier() {
    val gate = this.gate
    nextGeneration()
    try {
      barrierCommand?.invoke()
      gate.complete(Unit)
    } catch (e: CancellationException) {
      throw e
    } catch (e: Throwable) {
      gate.completeExceptionally(CyclicBarrierException(e))
    }
  }

  private fun nextGeneration() {
    this.waiting = 0
    this.gate = CompletableDeferred()
  }
}
