package kairo.coroutines

import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.emitAll

public suspend fun <T> FlowCollector<T>.emitAll(collection: Iterable<T>) {
  emitAll(collection.asFlow())
}
