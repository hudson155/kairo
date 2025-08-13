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
  @Suppress("UseDataClass")
  private class Generation(var broken: Boolean)

  private val lock: Mutex = Mutex()
  private var generation: Generation = Generation(broken = false)
  private var waiting: Int = 0
  private var gate: CompletableDeferred<Unit> = CompletableDeferred()

  init {
    require(parties > 0) { "Parties must be positive." }
  }

  public suspend fun await() {
    val (generation, gate, complete) = init()
    if (complete) {
      onComplete(gate)
    }
    await(generation, gate)
  }

  private suspend fun init(): Triple<Generation, CompletableDeferred<Unit>, Boolean> {
    var generation: Generation
    val gate: CompletableDeferred<Unit>
    var complete = false
    lock.withLock {
      this.waiting++
      generation = this.generation
      gate = this.gate
      if (this.waiting == this.parties) {
        nextGeneration()
        complete = true
      }
    }
    return Triple(generation, gate, complete)
  }

  private suspend fun onComplete(gate: CompletableDeferred<Unit>) {
    try {
      barrierCommand?.invoke()
      gate.complete(Unit)
    } catch (e: CancellationException) {
      gate.completeExceptionally(e)
      throw e
    } catch (e: Throwable) {
      gate.completeExceptionally(CyclicBarrierException(e))
    }
  }

  private suspend fun await(generation: Generation, gate: CompletableDeferred<Unit>) {
    try {
      gate.await()
    } catch (e: CancellationException) {
      var complete = false
      lock.withLock {
        if (!generation.broken) {
          nextGeneration()
          complete = true
        }
      }
      if (complete) {
        gate.completeExceptionally(e)
      }
      throw e
    }
  }

  private fun nextGeneration() {
    this.generation.broken = true
    this.generation = Generation(broken = false)
    this.waiting = 0
    this.gate = CompletableDeferred()
  }
}
