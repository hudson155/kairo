package kairo.coroutines

import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.flow.Flow

public suspend fun <T> Flow<T>.collect(collector: ProducerScope<T>) {
  collect { collector.send(it) }
}
