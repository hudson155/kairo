package kairo.admin.collector

import io.r2dbc.pool.ConnectionPool
import io.r2dbc.spi.ConnectionFactory
import kairo.admin.model.PoolStats

internal class PoolCollector(
  private val connectionFactoryProvider: () -> ConnectionFactory?,
) {
  fun collect(): PoolStats? {
    val factory = connectionFactoryProvider() ?: return null
    if (factory !is ConnectionPool) return null
    val metrics = factory.metrics.orElse(null) ?: return null
    return PoolStats(
      acquiredSize = metrics.acquiredSize(),
      allocatedSize = metrics.allocatedSize(),
      idleSize = metrics.idleSize(),
      pendingAcquireSize = metrics.pendingAcquireSize(),
      maxAllocatedSize = metrics.maxAllocatedSize,
      maxPendingAcquireSize = metrics.maxPendingAcquireSize,
    )
  }
}
