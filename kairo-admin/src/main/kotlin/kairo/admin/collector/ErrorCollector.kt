package kairo.admin.collector

import java.util.concurrent.ConcurrentLinkedDeque
import kairo.admin.model.ErrorRecord

public class ErrorCollector(
  private val maxSize: Int = 100,
) {
  private val errors: ConcurrentLinkedDeque<ErrorRecord> = ConcurrentLinkedDeque()

  public fun record(error: ErrorRecord) {
    errors.addFirst(error)
    while (errors.size > maxSize) {
      errors.removeLast()
    }
  }

  internal fun getAll(): List<ErrorRecord> =
    errors.toList()

  internal fun clear() {
    errors.clear()
  }
}
